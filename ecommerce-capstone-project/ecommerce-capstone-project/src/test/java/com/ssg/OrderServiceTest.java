package com.ssg;

import com.wipro.model.Cart;
import com.wipro.model.Customer;
import com.wipro.model.Order;
import com.wipro.model.Product;
import com.wipro.repository.CartRepository;
import com.wipro.repository.OrderRepository;
import com.wipro.service.OrderService;

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
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderService orderService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateOrder() {
        Customer customer = new Customer();
        List<Product> products = new ArrayList<>();
        String shippingAddress = "123 Street";
        Order order = new Order();

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(products, customer, shippingAddress);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testPlaceOrder() {
        Long cartId = 1L;
        String shippingAddress = "123 Street";
        Cart cart = new Cart();
        cart.setProducts(new ArrayList<>());
        cart.setQuantity(1);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        Order result = orderService.placeOrder(cartId, shippingAddress);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
