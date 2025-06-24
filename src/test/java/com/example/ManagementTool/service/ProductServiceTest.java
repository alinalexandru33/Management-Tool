package com.example.ManagementTool.service;

import com.example.ManagementTool.dto.CreateProductRequest;
import com.example.ManagementTool.dto.ProductDto;
import com.example.ManagementTool.dto.UpdateProductRequest;
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
    void updateProductChangePriceTest() {
        Product existing = Product.builder().id(5).name("Milk").price(7.0).quantity(10).build();
        Product saved = Product.builder().id(5).name("Milk").price(8.0).quantity(10).build();
        ProductDto dto = new ProductDto(5, "Milk", 8.0, 10);

        UpdateProductRequest updateReq = new UpdateProductRequest(8.0, null);

        when(productRepository.findById(5)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(saved);
        when(productMapper.toDto(saved)).thenReturn(dto);

        ProductDto result = productService.updateProduct(5, updateReq);

        assertEquals(dto, result);
        assertEquals(8.0, existing.getPrice());
        assertEquals(10, existing.getQuantity());
    }

    @Test
    void updateProductChangeQuantityTest() {
        Product existing = Product.builder().id(3).name("Milk").price(5.0).quantity(1).build();
        Product saved = Product.builder().id(3).name("Milk").price(5.0).quantity(33).build();
        ProductDto dto = new ProductDto(3, "Milk", 5.0, 33);

        UpdateProductRequest updateReq = new UpdateProductRequest(null, 33);

        when(productRepository.findById(3)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(saved);
        when(productMapper.toDto(saved)).thenReturn(dto);

        ProductDto result = productService.updateProduct(3, updateReq);

        assertEquals(dto, result);
        assertEquals(5.0, existing.getPrice());
        assertEquals(33, existing.getQuantity());
    }

    @Test
    void updateProductChangeBothTest() {
        Product existing = Product.builder().id(7).name("Milk").price(1.5).quantity(9).build();
        Product saved = Product.builder().id(7).name("Milk").price(2.0).quantity(10).build();
        ProductDto dto = new ProductDto(7, "Milk", 2.0, 10);

        UpdateProductRequest updateReq = new UpdateProductRequest(2.0, 10);

        when(productRepository.findById(7)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(saved);
        when(productMapper.toDto(saved)).thenReturn(dto);

        ProductDto result = productService.updateProduct(7, updateReq);

        assertEquals(dto, result);
        assertEquals(2.0, existing.getPrice());
        assertEquals(10, existing.getQuantity());
    }

    @Test
    void updateProductNotFoundTest() {
        UpdateProductRequest updateReq = new UpdateProductRequest(12.0, 3);
        when(productRepository.findById(100)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(100, updateReq));
    }
}
