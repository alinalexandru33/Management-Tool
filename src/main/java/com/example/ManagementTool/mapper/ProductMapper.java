package com.example.ManagementTool.mapper;

import com.example.ManagementTool.dto.CreateProductRequest;
import com.example.ManagementTool.dto.ProductDto;
import com.example.ManagementTool.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(CreateProductRequest request);
}
