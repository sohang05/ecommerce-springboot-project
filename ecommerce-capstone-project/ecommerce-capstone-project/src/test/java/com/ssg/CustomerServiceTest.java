package com.ssg;

import com.wipro.model.Customer;
import com.wipro.repository.CustomerRepository;
import com.wipro.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCustomer() {
        String name = "John";
        String email = "john@example.com";
        String password = "password";

        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer());

        Customer result = customerService.registerCustomer(name, email, password);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerByEmail() {
        String email = "john@example.com";
        Customer customer = new Customer();

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerByEmail(email);

        assertEquals(customer, result);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    
}
