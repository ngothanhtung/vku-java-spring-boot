package com.example.demo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class ChunkUploadController {

    private final Path tempRoot;
    private final Path finalRoot;

    // DTO for init request
    public static class InitRequest {
        private String filename;
        private Integer totalChunks;
        private Long totalSize;
        private String fileHash;

        // Getters and setters
        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public Integer getTotalChunks() {
            return totalChunks;
        }

        public void setTotalChunks(Integer totalChunks) {
            this.totalChunks = totalChunks;
        }

        public Long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(Long totalSize) {
            this.totalSize = totalSize;
        }

        public String getFileHash() {
            return fileHash;
        }

        public void setFileHash(String fileHash) {
            this.fileHash = fileHash;
        }
    }

    // Constructor
    public ChunkUploadController() {
        try {
            this.tempRoot = Paths.get(System.getProperty("java.io.tmpdir"), "chunks");

            // Use configurable upload directory or fallback to temp directory
            String uploadDir = System.getProperty("app.upload.directory",
                    System.getProperty("java.io.tmpdir") + "/uploads");
            this.finalRoot = Paths.get(uploadDir);

            Files.createDirectories(tempRoot);
            Files.createDirectories(finalRoot);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directories", e);
        }
    }

    @PostMapping(value = "/init", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> init(@RequestBody InitRequest request) {
        // Validate required fields
        if (request.getFilename() == null || request.getFilename().trim().isEmpty()) {
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("error", "Filename is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResp);
        }

        String uploadId = UUID.randomUUID().toString();
        Map<String, Object> resp = new HashMap<>();
        resp.put("uploadId", uploadId);
        resp.put("chunkSize", 5 * 1024 * 1024); // gợi ý 5MB
        resp.put("filename", request.getFilename());

        // Include optional parameters if provided
        if (request.getTotalChunks() != null) {
            resp.put("totalChunks", request.getTotalChunks());
        }
        if (request.getTotalSize() != null) {
            resp.put("totalSize", request.getTotalSize());
        }
        if (request.getFileHash() != null && !request.getFileHash().trim().isEmpty()) {
            resp.put("fileHash", request.getFileHash());
        }

        return ResponseEntity.ok(resp);
    }

    // Dùng để upload từng phần của file với hỗ trợ resume
    @PostMapping(value = "/chunk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadChunk(
            @RequestParam String uploadId,
            @RequestParam int chunkIndex,
            @RequestParam int totalChunks,
            @RequestParam String filename,
            @RequestPart("chunk") MultipartFile chunk,
            @RequestParam(required = false) String chunkHash, // optional
            @RequestParam(required = false, defaultValue = "false") boolean overwrite // allow overwriting existing
                                                                                      // chunks
    ) throws Exception {

        Path dir = tempRoot.resolve(uploadId);
        Files.createDirectories(dir);
        Path chunkPath = dir.resolve(String.format("%06d.part", chunkIndex));

        // Check if chunk already exists and handle accordingly
        if (Files.exists(chunkPath) && !overwrite) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("message", "Chunk already exists");
            resp.put("chunkIndex", chunkIndex);
            resp.put("skipped", true);
            resp.put("existing_size", Files.size(chunkPath));
            return ResponseEntity.ok(resp);
        }

        // Verify chunk hash if provided
        if (chunkHash != null && !chunkHash.trim().isEmpty()) {
            String actualHash = calcSha256(chunk.getBytes());
            if (!chunkHash.equalsIgnoreCase(actualHash)) {
                Map<String, Object> errorResp = new HashMap<>();
                errorResp.put("error", "Chunk hash verification failed");
                errorResp.put("expected", chunkHash);
                errorResp.put("actual", actualHash);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResp);
            }
        }

        // Save chunk atomically
        Path tmp = Files.createTempFile(dir, "part-", ".tmp");
        try (InputStream in = chunk.getInputStream()) {
            Files.copy(in, tmp, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        Files.move(tmp, chunkPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                java.nio.file.StandardCopyOption.ATOMIC_MOVE);

        Map<String, Object> ok = new HashMap<>();
        ok.put("received", chunkIndex);
        ok.put("size", chunk.getSize());
        ok.put("filename", filename);
        ok.put("uploadId", uploadId);
        ok.put("progress", String.format("%.2f%%", ((chunkIndex + 1) * 100.0) / totalChunks));
        return ResponseEntity.ok(ok);
    }

    // Dùng để kiểm tra trạng thái upload và hỗ trợ resume
    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> status(@RequestParam String uploadId) {
        try {
            Path dir = tempRoot.resolve(uploadId);
            List<Integer> have = new ArrayList<>();
            long totalUploadedSize = 0;

            if (Files.isDirectory(dir)) {
                try (var s = Files.list(dir)) {
                    s.filter(p -> p.getFileName().toString().endsWith(".part"))
                            .forEach(p -> {
                                try {
                                    String name = p.getFileName().toString();
                                    int idx = Integer.parseInt(name.substring(0, 6));
                                    have.add(idx);
                                } catch (NumberFormatException e) {
                                    // Skip invalid chunk files
                                }
                            });
                }

                // Calculate total uploaded size
                for (Integer chunkIndex : have) {
                    Path chunkPath = dir.resolve(String.format("%06d.part", chunkIndex));
                    try {
                        totalUploadedSize += Files.size(chunkPath);
                    } catch (IOException e) {
                        // Chunk file might be corrupted, ignore for size calculation
                    }
                }
            }

            // Sort chunks for better readability
            have.sort(Integer::compareTo);

            Map<String, Object> resp = new HashMap<>();
            resp.put("uploadId", uploadId);
            resp.put("chunks", have);
            resp.put("totalChunks", have.size());
            resp.put("uploadedSize", totalUploadedSize);
            resp.put("canResume", !have.isEmpty());

            // Calculate missing chunks if we know the total expected
            // This would require storing metadata, for now just return what we have
            resp.put("status", have.isEmpty() ? "not_started" : "in_progress");

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("error", "Failed to get upload status");
            errorResp.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResp);
        }
    }

    // Endpoint to check missing chunks for resume functionality
    @GetMapping(value = "/resume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> resume(
            @RequestParam String uploadId,
            @RequestParam int totalChunks) {
        try {
            Path dir = tempRoot.resolve(uploadId);
            List<Integer> have = new ArrayList<>();
            List<Integer> missing = new ArrayList<>();

            if (Files.isDirectory(dir)) {
                try (var s = Files.list(dir)) {
                    s.filter(p -> p.getFileName().toString().endsWith(".part"))
                            .forEach(p -> {
                                try {
                                    String name = p.getFileName().toString();
                                    int idx = Integer.parseInt(name.substring(0, 6));
                                    have.add(idx);
                                } catch (NumberFormatException e) {
                                    // Skip invalid chunk files
                                }
                            });
                }
            }

            // Calculate missing chunks
            for (int i = 0; i < totalChunks; i++) {
                if (!have.contains(i)) {
                    missing.add(i);
                }
            }

            have.sort(Integer::compareTo);
            missing.sort(Integer::compareTo);

            Map<String, Object> resp = new HashMap<>();
            resp.put("uploadId", uploadId);
            resp.put("totalChunks", totalChunks);
            resp.put("completedChunks", have);
            resp.put("missingChunks", missing);
            resp.put("progress", String.format("%.2f%%", (have.size() * 100.0) / totalChunks));
            resp.put("canResume", true);
            resp.put("isComplete", missing.isEmpty());

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("error", "Failed to get resume information");
            errorResp.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResp);
        }
    }

    // Endpoint to cleanup abandoned uploads
    @PostMapping(value = "/cleanup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> cleanup(@RequestParam String uploadId) {
        try {
            Path dir = tempRoot.resolve(uploadId);

            if (!Files.isDirectory(dir)) {
                Map<String, Object> resp = new HashMap<>();
                resp.put("message", "Upload directory not found");
                resp.put("uploadId", uploadId);
                return ResponseEntity.ok(resp);
            }

            // Remove all chunk files
            int deletedFiles = 0;
            try (var s = Files.list(dir)) {
                for (Path p : s.toList()) {
                    try {
                        Files.deleteIfExists(p);
                        deletedFiles++;
                    } catch (IOException e) {
                        // Log warning but continue cleanup
                    }
                }
            }

            // Remove directory
            Files.deleteIfExists(dir);

            Map<String, Object> resp = new HashMap<>();
            resp.put("message", "Upload cleaned up successfully");
            resp.put("uploadId", uploadId);
            resp.put("deletedFiles", deletedFiles);
            return ResponseEntity.ok(resp);

        } catch (IOException e) {
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("error", "Failed to cleanup upload");
            errorResp.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResp);
        }
    }

    // DTO for complete request
    public static class CompleteRequest {
        private String uploadId;
        private String filename;
        private String fileHash;

        // Getters and setters
        public String getUploadId() {
            return uploadId;
        }

        public void setUploadId(String uploadId) {
            this.uploadId = uploadId;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getFileHash() {
            return fileHash;
        }

        public void setFileHash(String fileHash) {
            this.fileHash = fileHash;
        }
    }

    @PostMapping(value = "/complete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> complete(@RequestBody CompleteRequest request) {
        try {
            // Validate required fields
            if (request.getUploadId() == null || request.getUploadId().trim().isEmpty()) {
                Map<String, Object> errorResp = new HashMap<>();
                errorResp.put("error", "Upload ID is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResp);
            }

            if (request.getFilename() == null || request.getFilename().trim().isEmpty()) {
                Map<String, Object> errorResp = new HashMap<>();
                errorResp.put("error", "Filename is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResp);
            }

            Path dir = tempRoot.resolve(request.getUploadId());
            if (!Files.isDirectory(dir)) {
                Map<String, Object> errorResp = new HashMap<>();
                errorResp.put("error", "Upload not found");
                errorResp.put("uploadId", request.getUploadId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResp);
            }

            // Merge chunks into final file
            Path finalPath = finalRoot.resolve(request.getFilename());
            try (OutputStream out = Files.newOutputStream(finalPath)) {
                var parts = Files.list(dir)
                        .filter(p -> p.getFileName().toString().endsWith(".part"))
                        .sorted(Comparator.comparing(Path::getFileName))
                        .toList();

                for (Path p : parts) {
                    Files.copy(p, out);
                }
            }

            // Verify whole file hash if provided
            if (request.getFileHash() != null && !request.getFileHash().trim().isEmpty()) {
                try {
                    String actualHash = sha256OfFile(finalPath);
                    if (!request.getFileHash().equalsIgnoreCase(actualHash)) {
                        Files.deleteIfExists(finalPath);
                        Map<String, Object> errorResp = new HashMap<>();
                        errorResp.put("error", "File hash verification failed");
                        errorResp.put("expected", request.getFileHash());
                        errorResp.put("actual", actualHash);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResp);
                    }
                } catch (IOException e) {
                    Files.deleteIfExists(finalPath);
                    Map<String, Object> errorResp = new HashMap<>();
                    errorResp.put("error", "Failed to verify file hash");
                    errorResp.put("message", e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResp);
                }
            }

            // Cleanup chunks
            try (var s = Files.list(dir)) {
                s.forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException ignored) {
                        // Log warning but continue cleanup
                    }
                });
            }
            Files.deleteIfExists(dir);

            Map<String, Object> resp = new HashMap<>();
            resp.put("file", request.getFilename());
            resp.put("path", finalPath.toString());
            resp.put("size", Files.size(finalPath));
            if (request.getFileHash() != null && !request.getFileHash().trim().isEmpty()) {
                resp.put("verified", true);
                resp.put("hash", request.getFileHash());
            }
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("error", "Failed to complete file upload");
            errorResp.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResp);
        }
    }

    /**
     * Calculate SHA-256 hash of a byte array
     */
    private String calcSha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Calculate SHA-256 hash of a file
     */
    private String sha256OfFile(Path filePath) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;

            try (InputStream inputStream = Files.newInputStream(filePath)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }

            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
