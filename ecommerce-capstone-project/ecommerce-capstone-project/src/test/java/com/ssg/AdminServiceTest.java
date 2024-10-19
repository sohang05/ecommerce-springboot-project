package com.ssg;

import com.wipro.model.Customer;
import com.wipro.repository.CustomerRepository;
import com.wipro.service.AdminService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignRole() {
        Long customerId = 1L;
        String role = "ADMIN";
        Customer customer = new Customer();
        customer.setId(customerId);
     
        when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        Customer result = adminService.assignRole(customerId, role);
        assertEquals(role, result.getRole());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(customer);
    }

    @Test
    void testAssignRoleInvalidRole() {
        Long customerId = 1L;
        String role = "INVALID_ROLE";
        
        assertThrows(IllegalArgumentException.class, () -> adminService.assignRole(customerId, role));
    }

    @Test
    void testAssignRoleCustomerNotFound() {
        Long customerId = 1L;
        String role = "ADMIN";
     
        when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.empty());
        
        assertThrows(RuntimeException.class, () -> adminService.assignRole(customerId, role));
    }
}
