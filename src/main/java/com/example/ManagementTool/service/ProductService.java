package com.example.ManagementTool.service;

import com.example.ManagementTool.dto.CreateProductRequest;
import com.example.ManagementTool.dto.ProductDto;
import com.example.ManagementTool.exception.ResourceAlreadyExistsException;
import com.example.ManagementTool.exception.ResourceNotFoundException;
import com.example.ManagementTool.mapper.ProductMapper;
import com.example.ManagementTool.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDto addProduct(CreateProductRequest request) {
        log.info("Adding product: {}", request);
        if(productRepository.findByName(request.name()).isPresent()) {
            throw new ResourceAlreadyExistsException("Product with this name exists");
        }

        var product = productMapper.toEntity(request);
        return productMapper.toDto(productRepository.save(product));
    }

    public ProductDto findByName(String name) {
        log.info("Searching for Product with name: {}", name);
        return productRepository.findByName(name)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional
    public ProductDto changePrice(Integer id, double newPrice) {
        log.info("Changing price of Product with id {} to {}", id, newPrice);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setPrice(newPrice);
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductDto changeQuantity(Integer id, int newQuantity) {
        log.info("Changing quantity of Product with id {} to {}", id, newQuantity);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setQuantity(newQuantity);
        return productMapper.toDto(productRepository.save(product));
    }
}
