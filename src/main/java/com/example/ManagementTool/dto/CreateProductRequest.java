package com.example.ManagementTool.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(
        @NotBlank(message = "Product name is required") String name,
        @Positive(message = "Price must be positive") double price,
        @Min(value = 0, message = "Quantity cannot be negative") int quantity
) {}
