package com.eshop.ecommerce.controller;

import com.eshop.ecommerce.model.cart.Cart;
import com.eshop.ecommerce.model.product.Product;
import com.eshop.ecommerce.model.user.User;
import com.eshop.ecommerce.repository.CartRepository;
import com.eshop.ecommerce.repository.UserRepository;
import com.eshop.ecommerce.service.CartService;
import com.eshop.ecommerce.service.ProductService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("ecommerce/v1/user")
public class UserController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get_all_products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("get_product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }



    @PostMapping("/add_to_cart/{productId}/{quantity}")
    public ResponseEntity<String> addProductToCart(@PathVariable Long productId, Principal principal, @PathVariable Integer quantity) {
        try {
            String username = principal.getName();
            Optional<User> user = userRepository.findByEmail(username);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }
            Product product = productService.getProductById(productId);
            if (product == null) {
                return ResponseEntity.badRequest().body("Product not found");
            }
            cartService.addProductToCart(username, productId,quantity);
            return ResponseEntity.ok("Product added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    @GetMapping("/cart_products")
    public ResponseEntity<List<Product>> getProductsInCart(Principal principal) {
        String username = principal.getName();
        List<Product> productsInCart = cartService.getProductsInCart(username);
        return ResponseEntity.ok(productsInCart);
    }
    @DeleteMapping("/remove_from_cart/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long productId, Principal principal) {
        try {
            String username = principal.getName();
            cartService.removeProductFromCart(username, productId);
            return ResponseEntity.ok("Product removed from cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/")
    public String getAdmin(){
        return "Welcome USER !";
    }
}
