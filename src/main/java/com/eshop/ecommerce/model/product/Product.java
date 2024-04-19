package com.eshop.ecommerce.model.product;

import com.eshop.ecommerce.model.cart.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String category;
    private Double price;
    private Double ratings;
    private Integer quantity;
    @JsonIgnore
    @OneToMany(mappedBy = "products")
    private List<Cart> carts = new ArrayList<>();
}
