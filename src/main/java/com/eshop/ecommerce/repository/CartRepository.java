package com.eshop.ecommerce.repository;

import com.eshop.ecommerce.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
