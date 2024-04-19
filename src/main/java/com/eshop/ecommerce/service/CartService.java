package com.eshop.ecommerce.service;

import com.eshop.ecommerce.model.cart.Cart;
import com.eshop.ecommerce.model.product.Product;
import com.eshop.ecommerce.model.user.User;
import com.eshop.ecommerce.repository.CartRepository;
import com.eshop.ecommerce.repository.ProductRepository;
import com.eshop.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addProductToCart(String username, Long productId, int quantity) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient quantity of the product");
        }
        product.setQuantity(product.getQuantity() - quantity);
        cart.getProducts().add(product);
        cartRepository.save(cart);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsInCart(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = user.getCart();
        if (cart == null || cart.getProducts().isEmpty()) {
            return Collections.emptyList(); // No products in cart
        }

        return cart.getProducts();
    }
    @Transactional
    public void removeProductFromCart(String username, Long productId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = user.getCart();
        if (cart != null) {
            Optional<Product> optionalProduct = cart.getProducts().stream()
                    .filter(product -> product.getId().equals(productId))
                    .findFirst();
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                cart.getProducts().remove(product);
                int quantityInCart = Collections.frequency(cart.getProducts(), product);
                product.setQuantity(product.getQuantity() + quantityInCart);
                productRepository.save(product);
                if (cart.getProducts().isEmpty()) {
                    cartRepository.delete(cart);
                } else {
                    cartRepository.save(cart);
                }
            }
        }
    }

}
