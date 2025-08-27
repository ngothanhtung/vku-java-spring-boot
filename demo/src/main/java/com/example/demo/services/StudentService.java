package com.example.demo.services;

import com.example.demo.dtos.*;
import com.example.demo.entities.Student;
import com.example.demo.enums.StudentStatus;
import com.example.demo.events.StudentCreatedEvent;
import com.example.demo.events.StudentDeletedEvent;
import com.example.demo.events.StudentUpdatedEvent;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repositories.StudentJpaRepository;
import com.example.demo.repositories.StudentProjection;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @PersistenceContext
    private EntityManager em;
    private final StudentJpaRepository studentJpaRepository;


    private final ApplicationEventPublisher eventPublisher;

    public StudentService(StudentJpaRepository studentJpaRepository, ApplicationEventPublisher eventPublisher) {
        this.studentJpaRepository = studentJpaRepository;
        this.eventPublisher = eventPublisher;
    }

    // create method convert entity to dto
    private StudentResponseDto convertToDto(Student student) {
        StudentResponseDto studentDto = new StudentResponseDto();
        studentDto.setId(student.getId());
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());
        studentDto.setAddress(student.getAddress());
        studentDto.setStatus(student.getStatus());
        if (student.getDepartment() != null) {
            DepartmentResponseDto departmentDto = new DepartmentResponseDto();
            departmentDto.setId(student.getDepartment().getId());
            departmentDto.setName(student.getDepartment().getName());
            studentDto.setDepartment(departmentDto);
        }
        if (student.getCourses() != null) {
            List<CourseResponseDto> courseDtos = student.getCourses().stream()
                    .map(course -> {
                        CourseResponseDto courseDto = new CourseResponseDto();
                        courseDto.setId(course.getId());
                        courseDto.setName(course.getName());
                        return courseDto;
                    })
                    .collect(Collectors.toList());
            studentDto.setCourses(courseDtos);
        }
        return studentDto;
    }

    @Cacheable(value = "students", key = "'all'")
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getAllStudents() {

        System.out.println("🔥 Fetching all students from the database...");

        List<Student> students = this.studentJpaRepository.getAllStudents();

        // Convert to DTOs
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Phương thức mới với phân trang
    @Cacheable(value = "students", key = "'paginated-' + #page + '-' + #size")
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

    @Cacheable(value = "students", key = "#id")
    public StudentResponseDto getStudentById(Long id) {
        Student student = this.studentJpaRepository.findById(id).orElseThrow();
        return convertToDto(student);
    }

    @CacheEvict(value = "students", allEntries = true)
    public StudentResponseDto createStudent(CreateStudentRequestDto createStudentRequestDto) {

        Student student = new Student();
        student.setName(createStudentRequestDto.getName());
        student.setEmail(createStudentRequestDto.getEmail());
        student.setAddress(createStudentRequestDto.getAddress());
        student.setPassword(createStudentRequestDto.getPassword());

        Student createdStudent = this.studentJpaRepository.save(student);

        // Phát sự kiện StudentCreatedEvent
        eventPublisher.publishEvent(new StudentCreatedEvent(createdStudent.getId(), createdStudent));
        return convertToDto(createdStudent);
    }

    @CachePut(value = "students", key = "#id")
    @CacheEvict(value = "students", key = "'all'")
    public StudentResponseDto updateStudent(Long id, UpdateStudentRequestDto student) {
        Student existingStudent = this.studentJpaRepository.findById(id).orElseThrow();
        existingStudent.setName(student.getName());
        existingStudent.setAddress(student.getAddress());
        Student updatedStudent = this.studentJpaRepository.save(existingStudent);

        // Phát sự kiện StudentUpdatedEvent
        eventPublisher.publishEvent(new StudentUpdatedEvent(updatedStudent.getId(), updatedStudent));

        return convertToDto(updatedStudent);
    }

    @CacheEvict(value = "students", allEntries = true)
    public void deleteStudent(Long id) {
//        this.studentJpaRepository.findById(id).orElseThrow(() -> new HttpException("No student present", HttpStatus.GONE));

        this.studentJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student"));
//        this.studentJpaRepository.findById(id).orElseThrow();
        this.studentJpaRepository.deleteById(id);
        // Phát sự kiện StudentDeletedEvent
        eventPublisher.publishEvent(new StudentDeletedEvent(id));
    }

    // JPA Specifications for Dynamic Queries
    @Cacheable(value = "students", key = "'name-' + #name")
    public List<Student> findByName(String name) {
        return this.studentJpaRepository.findAll(StudentSpecifications.hasName(name));
    }

    @Transactional
    public int updateStudentStatus(Long departmentId, String status) {
        return this.studentJpaRepository.updateStudentStatus(status, departmentId);
    }

    public List<StudentResponseDto> findAvailableStudents() {
        Session session = em.unwrap(Session.class);
        session.enableFilter("softDeleteFilter").setParameter("deleted", false);
        List<Student> student = em
                .createQuery("SELECT s FROM Student s LEFT JOIN FETCH s.department LEFT JOIN FETCH s.courses",
                        Student.class)
                .getResultList();
        session.disableFilter("softDeleteFilter");

        return student.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void softDeleteStudent(Long id) {
        Student student = this.studentJpaRepository.findById(id).orElseThrow();
        student.setDeleted(true);
        this.studentJpaRepository.save(student);
    }

    public List<StudentResponseDto> findByNotDeleted() {
        List<Student> students = this.studentJpaRepository.findByDeleted(false);

        // Convert to DTOs
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "students", key = "'status-' + #status")
    public List<StudentResponseDto> findByStatus(StudentStatus status) {

        List<Student> students = this.studentJpaRepository.findByStatus(status);

        // Convert to DTOs
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "students", key = "'department-' + #departmentId")
    public List<StudentResponseDto> findByDepartmentId(Long departmentId) {
        // Using EntityManager to fetch students by department ID
        EntityGraph<?> graph = em.createEntityGraph(Student.class);
        graph.addAttributeNodes("department", "courses");
        List<Student> students = em
                .createQuery("SELECT s FROM Student s WHERE s.department.id = :departmentId", Student.class)
                .setHint("javax.persistence.loadgraph", graph) // Sử dụng Entity Graph
                .setParameter("departmentId", departmentId)
                .getResultList();

        // Convert to DTOs
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // public List<StudentProjection> findByNameContainingIgnoreCase(String name) {
    // return this.studentJpaRepository.findByNameContainingIgnoreCase(name);
    // }

    public List<StudentProjection> findByNameContainingIgnoreCase(String name) {
        return this.studentJpaRepository.findByNameContainingIgnoreCase(name);
    }

    public List<StudentProjection> searchByEmailContainingIgnoreCase(String email) {
        return this.studentJpaRepository.searchByEmailContainingIgnoreCase(email);
    }
}
