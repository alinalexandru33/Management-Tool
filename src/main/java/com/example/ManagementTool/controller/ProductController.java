package com.example.ManagementTool.controller;

import com.example.ManagementTool.dto.CreateProductRequest;
import com.example.ManagementTool.dto.ProductDto;
import com.example.ManagementTool.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody CreateProductRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(req));
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ProductDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @PutMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> changePrice(@PathVariable Integer id, @RequestBody Double price) {
        return ResponseEntity.ok(productService.changePrice(id, price));
    }

    @PutMapping("/{id}/quantity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> changeQuantity(@PathVariable Integer id, @RequestBody Integer quantity) {
        return ResponseEntity.ok(productService.changeQuantity(id, quantity));
    }
}