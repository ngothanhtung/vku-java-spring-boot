package com.example.demo.services;

import com.example.demo.entities.Customer;
import com.example.demo.repositories.CustomerJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerJpaRepository customerJpaRepository;

    public CustomerService(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    public Customer createCustomer(Customer customer) {
        return this.customerJpaRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return this.customerJpaRepository.findById(id).orElseThrow();
    }

}
