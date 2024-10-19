package com.wipro.service;

import com.wipro.model.Cart;
import com.wipro.model.Customer;
import com.wipro.model.Order;
import com.wipro.model.Product;
import com.wipro.repository.CartRepository;
import com.wipro.repository.OrderRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;
    
    public Order createOrder(List<Product> products, Customer customer, String shippingAddress) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(new ArrayList<>(products)); 
        order.setShippingAddress(shippingAddress);
        order.setTotalAmount(calculateTotalAmount(products)); 
        return orderRepository.save(order);
    }

    private double calculateTotalAmount(List<Product> products) {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    @Transactional
    public Order placeOrder(Long cartId, String shippingAddress) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            Order order = new Order();
            order.setCustomer(cart.getCustomer());
            order.setProducts(new ArrayList<>(cart.getProducts()));
            order.setShippingAddress(shippingAddress);
            double totalAmount = cart.getProducts().stream().mapToDouble(Product::getPrice).sum() * cart.getQuantity();
            order.setTotalAmount(totalAmount); 
            return orderRepository.save(order);
        }
        return null;
    }
    
    public Order updateOrder(Long id, String shippingAddress) {
        
        Optional<Order> optionalOrder = orderRepository.findById(id);
        
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setShippingAddress("Shipping Address Updated Successfuly!!!"+shippingAddress); 
            return orderRepository.save(order); 
        } else {
            throw new RuntimeException("Order not found with cart ID: " + id);
        }
    }

   
    public String deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        
        if (optionalOrder.isPresent()) {
            orderRepository.delete(optionalOrder.get());
            return "Order with cart ID " + id + " deleted successfully.";
        } else {
            throw new RuntimeException("Order not found with cart ID: " + id);
        }
    }
}