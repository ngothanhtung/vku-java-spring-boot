package com.example.demo.utils;

import com.example.demo.enums.StudentStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// autoApply = true: This converter will be applied to all fields of type StudentStatus
// autoApply = false: This converter must be explicitly specified in the entity field
// If you want to use this converter for a specific field, 
// you must annotate that field with @Convert(converter = StudentStatusConverter.class)
@Converter(autoApply = false)
public class StudentStatusConverter implements AttributeConverter<StudentStatus, String> {
    @Override
    public String convertToDatabaseColumn(StudentStatus status) {
        return status != null ? status.getCode() : null;
    }

    @Override
    public StudentStatus convertToEntityAttribute(String code) {
        return code != null ? StudentStatus.fromCode(code) : null;
    }
}