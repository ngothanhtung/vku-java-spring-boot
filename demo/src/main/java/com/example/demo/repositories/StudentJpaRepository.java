package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Student;
import com.example.demo.enums.StudentStatus;

@Repository
public interface StudentJpaRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    // Avoid query N + 1 problem by using LEFT JOIN FETCH
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.department LEFT JOIN FETCH s.courses")
    List<Student> getAllStudents();

    // Avoid query N + 1 problem by using EntityGraph
    @EntityGraph(attributePaths = { "department", "courses" })
    List<Student> findByStatus(StudentStatus status);

    // Avoid query N + 1 problem by using EntityGraph
    @EntityGraph(value = "Student.WithDepartmentAndCourses", type = EntityGraph.EntityGraphType.LOAD)
    List<Student> findByDeleted(boolean deleted);

    // Batch update
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Student s SET s.status = :status WHERE s.department.id = :departmentId")
    int updateStudentStatus(@Param("status") String status, @Param("departmentId") Long departmentId);

    // Batch delete
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Student s WHERE s.status = :status")
    int deleteInactiveStudents(@Param("status") String status);

    // Search by name with projection
    // @Query("SELECT s.id AS id, s.name AS name, s.address AS address, s.status AS
    // status FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%',:name,
    // '%'))")
    // List<StudentProjection> findByNameContainingIgnoreCase(@Param("name") String
    // name);

    List<StudentProjection> findByNameContainingIgnoreCase(String name);

    // Other query methods
    List<StudentProjection> searchByEmailContainingIgnoreCase(String email);

    @Query(value = "SELECT s.id, s.name, s.email FROM students s", nativeQuery = true)
    List<Student> getAllStudents1();
}
