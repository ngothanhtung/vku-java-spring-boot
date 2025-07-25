package com.example.demo.services;

import com.example.demo.entities.Student;
import com.example.demo.repositories.StudentJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentJpaRepository studentJpaRepository;

    public StudentService(StudentJpaRepository studentJpaRepository) {
        this.studentJpaRepository = studentJpaRepository;
    }

    public List<Student> getAllCategories() {
        return this.studentJpaRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return this.studentJpaRepository.findById(id).orElseThrow();
    }

    public Student createStudent(Student student) {
        return this.studentJpaRepository.save(student);
    }

    public void updateStudent(Long id, Student student) {
        Student existingStudent = this.studentJpaRepository.findById(id).orElseThrow();
        existingStudent.setName(student.getName());
        this.studentJpaRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        this.studentJpaRepository.findById(id).orElseThrow();
        this.studentJpaRepository.deleteById(id);
    }
}
