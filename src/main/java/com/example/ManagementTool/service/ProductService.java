package com.example.ManagementTool.service;

import com.example.ManagementTool.dto.CreateProductRequest;
import com.example.ManagementTool.dto.ProductDto;
import com.example.ManagementTool.mapper.ProductMapper;
import com.example.ManagementTool.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDto addProduct(CreateProductRequest request) {
        if(productRepository.findByName(request.name()).isPresent()) {
            throw new IllegalArgumentException("Product with this name exists!");
        }

        var product = productMapper.toEntity(request);
        return productMapper.toDto(productRepository.save(product));
    }

    public ProductDto findByName(String name) {
        return productRepository.findByName(name)
                .map(productMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional
    public ProductDto changePrice(Integer id, double newPrice) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setPrice(newPrice);
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductDto changeQuantity(Integer id, int newQuantity) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setQuantity(newQuantity);
        return productMapper.toDto(productRepository.save(product));
    }
}
