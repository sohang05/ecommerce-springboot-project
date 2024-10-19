package com.ssg;

import com.wipro.model.Cart;
import com.wipro.model.Customer;
import com.wipro.model.Product;
import com.wipro.repository.CartRepository;
import com.wipro.repository.CustomerRepository;
import com.wipro.repository.ProductRepository;
import com.wipro.service.CartService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToCart_WithValidCustomerAndProduct() {
        Long customerId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Customer customer = new Customer();
        Product product = new Product();
        Cart cart = new Cart();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByCustomer(customer)).thenReturn(new ArrayList<>());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addProductToCart(customerId, productId, quantity);

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddProductToCart_WithInvalidQuantity() {
        Long customerId = 1L;
        Long productId = 1L;
        int quantity = 0;

        assertThrows(IllegalArgumentException.class, () ->
                cartService.addProductToCart(customerId, productId, quantity));
    }

    @Test
    void testAddProductToCart_CustomerOrProductNotFound() {
        Long customerId = 1L;
        Long productId = 1L;
        int quantity = 2;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                cartService.addProductToCart(customerId, productId, quantity));
    }

    @Test
    void testGetCartItemsByCustomerId_WithValidCustomer() {
        Long customerId = 1L;
        Customer customer = new Customer();
        List<Cart> carts = List.of(new Cart());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(cartRepository.findByCustomer(customer)).thenReturn(carts);

        List<Cart> result = cartService.getCartItemsByCustomerId(customerId);

        assertEquals(carts, result);
        verify(cartRepository, times(1)).findByCustomer(customer);
    }

    @Test
    void testGetCartItemsByCustomerId_WithInvalidCustomer() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        List<Cart> result = cartService.getCartItemsByCustomerId(customerId);

        assertTrue(result.isEmpty());
        verify(cartRepository, never()).findByCustomer(any());
    }

    @Test
    void testUpdateCartItemQuantity_WithValidCart() {
        Long cartId = 1L;
        int quantity = 3;
        Cart cart = new Cart();

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.updateCartItemQuantity(cartId, quantity);

        assertNotNull(result);
        assertEquals(quantity, result.getQuantity());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateCartItemQuantity_WithInvalidCart() {
        Long cartId = 1L;
        int quantity = 3;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                cartService.updateCartItemQuantity(cartId, quantity));
    }

    @Test
    void testDeleteCartItem() {
        Long cartId = 1L;

        doNothing().when(cartRepository).deleteById(cartId);

        cartService.deleteCartItem(cartId);

        verify(cartRepository, times(1)).deleteById(cartId);
    }
}
