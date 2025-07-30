package com.example.demo.repositories;

import com.example.demo.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentJpaRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.department")
    List<Student> getAllStudentsWithDepartment();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Student s SET s.status = :status WHERE s.department.id = :departmentId")
    int updateStudentStatus(@Param("status") String status, @Param("departmentId") Long departmentId);

    @Modifying
    @Query("DELETE FROM Student s WHERE s.status = :status")
    int deleteInactiveStudents(@Param("status") String status);
}
