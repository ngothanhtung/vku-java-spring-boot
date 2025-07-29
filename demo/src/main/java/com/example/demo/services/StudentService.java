package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Student;
import com.example.demo.repositories.StudentJpaRepository;

@Service
public class StudentService {

    private final StudentJpaRepository studentJpaRepository;

    public StudentService(StudentJpaRepository studentJpaRepository) {
        this.studentJpaRepository = studentJpaRepository;
    }

    // create method convert entity to dto
    private StudentResponseDto convertToDto(Student student) {
        StudentResponseDto studentDto = new StudentResponseDto();
        studentDto.setId(student.getId());
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());
        studentDto.setAddress(student.getAddress());
        if (student.getDepartment() != null) {
            DepartmentResponseDto departmentDto = new DepartmentResponseDto();
            departmentDto.setId(student.getDepartment().getId());
            departmentDto.setName(student.getDepartment().getName());
            studentDto.setDepartment(departmentDto);
        }
        return studentDto;
    }

    public List<StudentResponseDto> getAllStudent() {
        List<Student> students = this.studentJpaRepository.getAllStudentsWithDepartment();


        // Convert to DTOs
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Phương thức mới với phân trang
    public PaginatedStudentResponseDto getAllStudentsPaginated(int page, int size) {
        // Tạo Pageable object với page và size
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu phân trang từ repository
        Page<Student> studentPage = this.studentJpaRepository.findAll(pageable);

        // Chuyển đổi Page<Student> thành List<StudentResponseDto>
        List<StudentResponseDto> studentDtos = studentPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Tạo response DTO với thông tin phân trang
        return PaginatedStudentResponseDto.builder()
                .data(studentDtos)
                .pageNumber(studentPage.getNumber())
                .pageSize(studentPage.getSize())
                .totalRecords(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .hasNext(studentPage.hasNext())
                .hasPrevious(studentPage.hasPrevious())
                .build();
    }

    public StudentResponseDto getStudentById(Long id) {
        Student student = this.studentJpaRepository.findById(id).orElseThrow();
        return convertToDto(student);
    }

    public StudentResponseDto createStudent(CreateStudentRequestDto createStudentRequestDto) {

        Student student = new Student();
        student.setName(createStudentRequestDto.getName());
        student.setEmail(createStudentRequestDto.getEmail());
        student.setAddress(createStudentRequestDto.getAddress());
        student.setPassword(createStudentRequestDto.getPassword());

        Student createdStudent = this.studentJpaRepository.save(student);
        return convertToDto(createdStudent);
    }

    public StudentResponseDto updateStudent(Long id, UpdateStudentRequestDto student) {
        Student existingStudent = this.studentJpaRepository.findById(id).orElseThrow();
        existingStudent.setName(student.getName());
        existingStudent.setAddress(student.getAddress());
        Student updatedStudent = this.studentJpaRepository.save(existingStudent);
        return convertToDto(updatedStudent);
    }

    public void deleteStudent(Long id) {
        this.studentJpaRepository.findById(id).orElseThrow();
        this.studentJpaRepository.deleteById(id);
    }
}
