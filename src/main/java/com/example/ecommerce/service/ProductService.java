package com.example.ecommerce.service;

import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product, Long categoryId);
    ProductResponse getProduct(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    ProductResponse getByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    ProductResponse searchProductsByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductDTO updateProduct(ProductDTO product, Long productId);

    ProductDTO deleteProduct(Long productId);


    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
