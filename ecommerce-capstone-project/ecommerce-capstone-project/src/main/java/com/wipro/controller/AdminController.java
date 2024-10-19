package com.wipro.controller;

import com.wipro.model.Cart;
import com.wipro.model.Customer;
import com.wipro.model.Product;
import com.wipro.service.AdminService;
import com.wipro.service.CartService;
import com.wipro.service.CustomerService;
import com.wipro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private Environment env;

    @PostMapping("/login")
    public String adminLogin(@RequestParam String username, @RequestParam String password, Model model) {
        // Validate admin credentials
        if (username.equals(env.getProperty("admin.username")) && password.equals(env.getProperty("admin.password"))) {
            return "redirect:/admin/dashboard";  // Redirect to admin dashboard
        } else {
            model.addAttribute("errorMessage", "Admin not found");
            return "admin-login";  // Show error message on login page
        }
    }

    
    @PostMapping("/assign-role/{customerId}")
    public Customer assignRole(@PathVariable Long customerId, @RequestParam String role) {
        return adminService.assignRole(customerId, role);
    }
    
    // Customer Endpoints
    @PostMapping("/customers")
    public List<Customer> addCustomers(@RequestBody List<Customer> customers) {
        return customerService.addCustomers(customers);
    }

    @PostMapping("/customer")
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PutMapping("/customer/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        
    }

    // Product Endpoints
    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return productService.addOrUpdateProduct(product);
    }

    @PostMapping("/products/multiple")
    public List<Product> addMultipleProducts(@RequestBody List<Product> products) {
        return productService.addOrUpdateProducts(products);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/product/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id); 
        return productService.addOrUpdateProduct(product);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/customer/{customerId}/cart")
    public ResponseEntity<List<Cart>> getCartItemsByCustomerId(@PathVariable Long customerId) {
        List<Cart> cartItems = cartService.getCartItemsByCustomerId(customerId);
        return ResponseEntity.ok(cartItems);
    }
}