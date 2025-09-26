package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MockitoExampleServiceTest {

  private MockitoExampleService service;

  @BeforeEach
  void setUp() {
    service = new MockitoExampleService();
  }

  @Test
  void testAdd() {
    assertEquals(5, service.Add(2, 3));
  }

  @Test
  void testSubtract() {
    assertEquals(1, service.Subtract(3, 2));
  }

  @Test
  void testMultiply() {
    assertEquals(6, service.Multiply(2, 3));
  }

  @Test
  void testDivide() {
    assertEquals(2, service.Divide(6, 3));
  }

  @Test
  void testDivideByZero() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> service.Divide(5, 0));
    assertEquals("Division by zero is not allowed.", exception.getMessage());
  }
}
