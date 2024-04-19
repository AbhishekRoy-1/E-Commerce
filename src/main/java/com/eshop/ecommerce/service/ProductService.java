package com.eshop.ecommerce.service;

import com.eshop.ecommerce.exceptions.ProductNotFoundException;
import com.eshop.ecommerce.model.product.Product;
import com.eshop.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Transactional
    public void importDataFromCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 5) {
                    Product product = getProduct(data);
                    productRepository.save(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Product getProduct(String[] data) {
        String productName = data[0].trim();
        String category = data[1].trim();
        Double price = Double.parseDouble(data[2].trim());
        Double ratings = Double.parseDouble(data[3].trim());
        Integer quantity = Integer.parseInt(data[4].trim());
        Product product = new Product();
        product.setProductName(productName);
        product.setCategory(category);
        product.setPrice(price);
        product.setRatings(ratings);
        product.setQuantity(quantity);
        return product;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }


    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setProductName(productDetails.getProductName());
        product.setCategory(productDetails.getCategory());
        product.setPrice(productDetails.getPrice());
        product.setRatings(productDetails.getRatings());
        product.setQuantity(productDetails.getQuantity());
        return productRepository.save(product);
    }
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }


}