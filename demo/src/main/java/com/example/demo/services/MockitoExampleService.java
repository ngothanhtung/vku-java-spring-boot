
package com.example.demo.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MockitoExampleService {

  public Integer Add(Integer a, Integer b) {
    return a + b;
  }

  public Integer Subtract(Integer a, Integer b) {
    return a - b;
  }

  public Integer Multiply(Integer a, Integer b) {
    return a * b;
  }

  public Integer Divide(Integer a, Integer b) {
    if (b == 0) {
      throw new IllegalArgumentException("Division by zero is not allowed.");
    }
    return a / b;
  }
}
