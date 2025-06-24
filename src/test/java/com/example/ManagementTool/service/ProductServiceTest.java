package com.example.ManagementTool.service;

import com.example.ManagementTool.dto.CreateProductRequest;
import com.example.ManagementTool.dto.ProductDto;
import com.example.ManagementTool.exception.ResourceAlreadyExistsException;
import com.example.ManagementTool.exception.ResourceNotFoundException;
import com.example.ManagementTool.mapper.ProductMapper;
import com.example.ManagementTool.model.Product;
import com.example.ManagementTool.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void addProductSuccessTest() {
        CreateProductRequest req = new CreateProductRequest("Milk", 4.5, 100);
        Product product = Product.builder().name("Milk").price(4.5).quantity(100).build();
        Product savedProduct = Product.builder().id(1).name("Milk").price(4.5).quantity(100).build();
        ProductDto productDto = new ProductDto(1, "Milk", 4.5, 100);

        when(productRepository.findByName("Milk")).thenReturn(Optional.empty());
        when(productMapper.toEntity(req)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(productDto);

        ProductDto result = productService.addProduct(req);

        assertEquals(productDto, result);
        verify(productRepository).save(product);
    }

    @Test
    void addProductAlreadyExistsTest() {
        CreateProductRequest req = new CreateProductRequest("Milk", 4.5, 100);

        when(productRepository.findByName("Milk")).thenReturn(Optional.of(new Product()));

        assertThrows(ResourceAlreadyExistsException.class, () -> productService.addProduct(req));
        verify(productRepository, never()).save(any());
    }

    @Test
    void findByNameSuccessTest() {
        Product product = Product.builder().id(2).name("Milk").price(2.0).quantity(33).build();
        ProductDto dto = new ProductDto(2, "Milk", 2.0, 33);

        when(productRepository.findByName("Milk")).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(dto);

        ProductDto result = productService.findByName("Milk");

        assertEquals(dto, result);
    }

    @Test
    void findByNameNotFoundTest() {
        when(productRepository.findByName("Unknown")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.findByName("Unknown"));
    }

    @Test
    void findAllSuccessTest() {
        List<Product> products = List.of(
                Product.builder().id(1).name("Milk").price(1.0).quantity(1).build(),
                Product.builder().id(2).name("Soda").price(2.0).quantity(2).build()
        );
        List<ProductDto> dtos = List.of(
                new ProductDto(1, "Milk", 1.0, 1),
                new ProductDto(2, "Soda", 2.0, 2)
        );

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(products.get(0))).thenReturn(dtos.get(0));
        when(productMapper.toDto(products.get(1))).thenReturn(dtos.get(1));

        List<ProductDto> result = productService.findAll();

        assertEquals(dtos, result);
    }

    @Test
    void changePriceSuccessTest() {
        Product existing = Product.builder().id(5).name("Milk").price(7.0).quantity(10).build();
        Product saved = Product.builder().id(5).name("Milk").price(8.0).quantity(10).build();
        ProductDto dto = new ProductDto(5, "Milk", 8.0, 10);

        when(productRepository.findById(5)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(saved);
        when(productMapper.toDto(saved)).thenReturn(dto);

        ProductDto result = productService.changePrice(5, 8.0);

        assertEquals(dto, result);
        assertEquals(8.0, existing.getPrice());
    }

    @Test
    void changePriceNotFoundTest() {
        when(productRepository.findById(123)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.changePrice(123, 11.0));
    }

    @Test
    void changeQuantitySuccessTest() {
        Product existing = Product.builder().id(3).name("Milk").price(5.0).quantity(1).build();
        Product saved = Product.builder().id(3).name("Milk").price(5.0).quantity(33).build();
        ProductDto dto = new ProductDto(3, "Milk", 5.0, 33);

        when(productRepository.findById(3)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(saved);
        when(productMapper.toDto(saved)).thenReturn(dto);

        ProductDto result = productService.changeQuantity(3, 33);

        assertEquals(dto, result);
        assertEquals(33, existing.getQuantity());
    }

    @Test
    void changeQuantityNotFoundTest() {
        when(productRepository.findById(123)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.changePrice(123, 11.0));
    }
}
