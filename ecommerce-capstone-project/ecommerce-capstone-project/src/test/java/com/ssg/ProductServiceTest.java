package com.ssg;

import com.wipro.model.Product;
import com.wipro.repository.ProductRepository;
import com.wipro.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(new Product()));

        List<Product> products = productService.getAllProducts();

        assertFalse(products.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testAddOrUpdateProduct() {
        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addOrUpdateProduct(product);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }

    // Additional tests for other methods can be added similarly
}
