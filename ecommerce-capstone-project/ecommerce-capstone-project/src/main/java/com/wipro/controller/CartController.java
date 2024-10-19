package com.wipro.controller;

import com.wipro.model.AddToCartRequest;
import com.wipro.model.Cart;
import com.wipro.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Cart>> getCartItemsByCustomerId(@PathVariable Long customerId) {
        List<Cart> cartItems = cartService.getCartItemsByCustomerId(customerId);
        return ResponseEntity.ok(cartItems);
    }

    
    @PostMapping("/add-to-cart")
    public ResponseEntity<Cart> addProductToCart(@RequestBody AddToCartRequest addToCartRequest) {
        Long customerId = addToCartRequest.getCustomerId();
        Long productId = addToCartRequest.getProductId();
        int quantity = addToCartRequest.getQuantity();

        Cart cartItem = cartService.addProductToCart(customerId, productId, quantity);
        return ResponseEntity.ok(cartItem);
    }


    @PutMapping("/update-quantity")
    public ResponseEntity<Cart> updateCartItemQuantity(@RequestParam Long cartId, @RequestParam int quantity) {
        Cart updatedCartItem = cartService.updateCartItemQuantity(cartId, quantity);
        return ResponseEntity.ok(updatedCartItem);
    }

    
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCartItem(@RequestParam Long cartId) {
        cartService.deleteCartItem(cartId);
        return ResponseEntity.ok("Cart item removed successfully.");
    }

}