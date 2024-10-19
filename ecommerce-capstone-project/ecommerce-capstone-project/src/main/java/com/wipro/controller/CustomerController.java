package com.wipro.controller;

import com.wipro.model.Customer;
import com.wipro.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

//    @PostMapping("/register")
//    @PreAuthorize("hasRole('CUSTOMER')")
//    public Customer registerCustomer(@RequestBody Customer customer) {
//        return customerService.registerCustomer(customer.getName(), customer.getEmail(), customer.getPassword());
//    }
//    
//    @PostMapping("/login")
//    public String userLogin(@RequestParam String email, @RequestParam String password, Model model) {
//        Optional<Customer> customer = customerService.findByEmail(email);
//        if (customer.isPresent() && passwordEncoder.matches(password, customer.get().getPassword())) {
//            return "redirect:/shopping";  // Redirect to shopping page
//        } else {
//            model.addAttribute("errorMessage", "User not found");
//            return "user-login-register";  // Show error message on the same page
//        }
//    }
    
 // Display the user login page
    @GetMapping("/user/login")
    public String userLoginPage(Model model) {
        return "user-login"; // Loads user-login.html
    }

    // Display the user registration page
    @GetMapping("/user/register")
    public String userRegisterPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "user-register"; // Loads user-register.html
    }

    // Handle user registration
    @PostMapping("/user/register")
    public String registerUser(@ModelAttribute("customer") Customer customer, Model model) {
        try {
            customerService.registerCustomer(customer.getName(), customer.getEmail(), customer.getPassword());
            return "redirect:/shopping/home"; // Redirect to login page after successful registration
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user-register"; // Stay on registration page if an error occurs
        }
    }

    // Handle user login
    @PostMapping("/user/login")
    public String userLogin(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            // Assuming you have a method to validate user login in CustomerService
            Customer customer = customerService.getCustomerByEmail(email);
            if (passwordEncoder.matches(password, customer.getPassword())) {
                return "redirect:/shopping/home"; // Redirect to shopping page after successful login
            } else {
                model.addAttribute("error", "Invalid credentials.");
                return "user-login"; // Stay on login page if credentials are invalid
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", "User not found.");
            return "user-login"; // Stay on login page if user not found
        }
    }
    
}