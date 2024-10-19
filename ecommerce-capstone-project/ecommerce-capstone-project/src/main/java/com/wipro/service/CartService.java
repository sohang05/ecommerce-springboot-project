package com.wipro.service;

import com.wipro.model.Cart;
import com.wipro.model.Customer;
import com.wipro.model.Product;
import com.wipro.repository.CartRepository;
import com.wipro.repository.CustomerRepository;
import com.wipro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart addProductToCart(Long customerId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (customerOpt.isPresent() && productOpt.isPresent()) {
            Customer customer = customerOpt.get();
            Product product = productOpt.get();
            List<Cart> existingCarts = cartRepository.findByCustomer(customer);

            Cart cart;
            if (existingCarts.isEmpty()) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart.setProducts(new ArrayList<>());
            } else {
                cart = existingCarts.get(0);
            }

            cart.getProducts().add(product);
            cart.setQuantity(cart.getQuantity() + quantity);

            return cartRepository.save(cart);
        } else {
            throw new RuntimeException("Customer or Product not found");
        }
    }


    
    public List<Cart> getCartItemsByCustomerId(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            return cartRepository.findByCustomer(customerOpt.get());
        }
        return new ArrayList<>();
    }

    
    public Cart updateCartItemQuantity(Long cartId, int quantity) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.setQuantity(quantity);
            return cartRepository.save(cart);
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    public void deleteCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

 
}