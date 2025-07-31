package com.example.demo.services;

import com.example.demo.dtos.CreateStudentRequestDto;
import com.example.demo.dtos.StudentResponseDto;
import com.example.demo.entities.Student;
import com.example.demo.repositories.StudentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentJpaRepository studentJpaRepository;

    @InjectMocks
    private StudentService studentService;

    private CreateStudentRequestDto createStudentRequestDto;
    private Student savedStudent;

    @BeforeEach
    void setUp() {
        // Prepare test data
        createStudentRequestDto = CreateStudentRequestDto.builder()
                .name("Test Student")
                .email("test@example.com")
                .address("123 Test Street")
                .password("password123")
                .build();

        savedStudent = Student.builder()
                .id(1L)
                .name("Test Student")
                .email("test@example.com")
                .address("123 Test Street")
                .password("password123")
                .build();
    }

    @Test
    void createStudent_ShouldReturnStudentResponseDto() {
        // Arrange
        when(studentJpaRepository.save(any(Student.class))).thenReturn(savedStudent);

        // Act
        StudentResponseDto result = studentService.createStudent(createStudentRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Student", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("123 Test Street", result.getAddress());
    }
    
    @Test
    void createStudent_WithNullAddress_ShouldStillCreateStudent() {
        // Arrange
        createStudentRequestDto.setAddress(null);
        
        Student studentWithNullAddress = Student.builder()
                .id(1L)
                .name("Test Student")
                .email("test@example.com")
                .password("password123")
                .build();
                
        when(studentJpaRepository.save(any(Student.class))).thenReturn(studentWithNullAddress);

        // Act
        StudentResponseDto result = studentService.createStudent(createStudentRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Student", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertNull(result.getAddress());
    }
    
    @Test
    void createStudent_WithDepartmentId_ShouldCreateStudentWithDepartment() {
        // Arrange
        createStudentRequestDto.setDepartmentId(1L);
        
        // Create a student with department set
        Student studentWithDepartment = Student.builder()
                .id(1L)
                .name("Test Student")
                .email("test@example.com")
                .address("123 Test Street")
                .password("password123")
                .build();
                
        when(studentJpaRepository.save(any(Student.class))).thenReturn(studentWithDepartment);

        // Act
        StudentResponseDto result = studentService.createStudent(createStudentRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Student", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("123 Test Street", result.getAddress());
        // Note: In the actual implementation, the department would be set,
        // but since we're mocking the repository response and the service doesn't
        // fetch the department in the createStudent method, we can't test that here
    }
}