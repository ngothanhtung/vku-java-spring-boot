package com.example.demo.entities;

import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.example.demo.enums.StudentStatus;
import com.example.demo.utils.StudentStatusConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(name = "Student.WithDepartmentAndCourses", attributeNodes = {
		@NamedAttributeNode("courses"),
		@NamedAttributeNode("department")
})
@NamedEntityGraph(name = "Student.WithCourses", attributeNodes = {
		@NamedAttributeNode("courses")
})
@NamedEntityGraph(name = "Student.WithDepartment", attributeNodes = {
		@NamedAttributeNode("department")
})
@Table(name = "students")
@FilterDef(name = "softDeleteFilter", parameters = @ParamDef(name = "deleted", type = Boolean.class))
@Filter(name = "softDeleteFilter", condition = "deleted = :deleted")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String name;

	@Column(length = 100, nullable = false)
	private String email;

	@Column(length = 500, nullable = true)
	private String address;

	@Column(length = 100, nullable = false)
	private String password;

	// Check age >= 18
	@Column(nullable = true, columnDefinition = "int check (age >= 18)")
	private int age;

	// check status in: ACTIVE, INACTIVE, SUSPENDED

	@Convert(converter = StudentStatusConverter.class)
	private StudentStatus status;

	private boolean deleted;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	private Department department;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "student_courses", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private List<Course> courses;
}
