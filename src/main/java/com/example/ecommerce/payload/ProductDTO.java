package com.example.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDTO {
    private Long productId;
    private String productName;
    private String image;
    private double discount;
    private Integer quantity;
    private double price;
    private double specialPrice;
    private String description;
}
