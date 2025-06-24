package com.example.ManagementTool.service;

import com.example.ManagementTool.model.Product;
import com.example.ManagementTool.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product changePrice(Integer id, double newPrice) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

    public Product changeQuantity(Integer id, int newQuantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setQuantity(newQuantity);
        return productRepository.save(product);
    }
}
