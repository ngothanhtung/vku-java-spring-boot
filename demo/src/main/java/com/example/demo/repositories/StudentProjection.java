package com.example.demo.repositories;

import com.example.demo.enums.StudentStatus;

public interface StudentProjection {
  Long getId();

  String getName();

  String getAddress();

  String getEmail();

  StudentStatus getStatus();
}