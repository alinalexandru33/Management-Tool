package com.example.ManagementTool.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record UpdateProductRequest(
        @Positive(message = "Price must be positive") Double price,
        @Min(value = 0, message = "Quantity must be non-negative") Integer quantity
) {}