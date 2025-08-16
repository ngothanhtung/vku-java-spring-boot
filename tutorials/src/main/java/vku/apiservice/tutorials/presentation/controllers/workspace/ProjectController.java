package vku.apiservice.tutorials.presentation.controllers.workspace;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vku.apiservice.tutorials.application.dtos.workspace.CreateProjectRequestDto;
import vku.apiservice.tutorials.application.dtos.workspace.UpdateProjectRequestDto;
import vku.apiservice.tutorials.application.services.workspace.ProjectApplicationService;
import vku.apiservice.tutorials.infrastructure.config.PreAuthorizeUtil;

@RestController
@RequestMapping("/api/workspace/projects")
@PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER)
public class ProjectController {
    private final ProjectApplicationService projectApplicationService;

    public ProjectController(ProjectApplicationService projectApplicationService) {
        this.projectApplicationService = projectApplicationService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid CreateProjectRequestDto data) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectApplicationService.createProject(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable("id") String id, @RequestBody @Valid UpdateProjectRequestDto data) {
        return ResponseEntity.ok(this.projectApplicationService.updateProject(id, data));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchProject(@PathVariable("id") String id, @RequestBody @Valid UpdateProjectRequestDto data) {
        return ResponseEntity.ok(this.projectApplicationService.updateProject(id, data));
    }

    @GetMapping
    public ResponseEntity<?> getProjects(@RequestParam(value = "includeTasks", defaultValue = "false") boolean includeTasks) {
        if (includeTasks) {
            return ResponseEntity.ok(projectApplicationService.getProjectsWithTasks());
        }
        return ResponseEntity.ok(projectApplicationService.getProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable("id") String id) {
        return ResponseEntity.ok(this.projectApplicationService.getProjectById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") String id) {
        projectApplicationService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
