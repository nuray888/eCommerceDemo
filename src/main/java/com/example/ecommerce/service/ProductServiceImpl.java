package com.example.ecommerce.service;

import com.example.ecommerce.exceptions.APIException;
import com.example.ecommerce.exceptions.ResourceNotFoundexception;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new  ResourceNotFoundexception("Category","Category",categoryId));
        boolean isProductNotPresent=true;
        List<Product> products=category.getProducts();
        for(Product value:products){
            if(value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if(isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setCategory(category);
            product.setImage("default.png");
            double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() * 0.01);
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else{
            throw new APIException("Product is already exist");
        }
    }

    public ProductResponse getProduct(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder){
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProduct = productRepository.findAll(pageDetails);
        List<Product> lists=pageProduct.getContent();
        List<ProductDTO> list = lists.stream().map(p -> modelMapper.map(p, ProductDTO.class)).toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(list);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElements(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());
        return productResponse;
    }
    public ProductResponse getByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder){
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new  ResourceNotFoundexception("Category","CategoryId",categoryId));
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProduct = productRepository.getProductByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> products=pageProduct.getContent();
        if(products.isEmpty()){
            throw new APIException(category.getCategoryName()+" category does not have any products");
        }
        List<ProductDTO> list = products.stream().map(p -> modelMapper.map(p, ProductDTO.class)).toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(list);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElements(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProduct = productRepository.findByProductNameLikeIgnoreCase(keyword,pageDetails);
        List<Product> products=pageProduct.getContent();
        List<ProductDTO> list = products.stream().map(p -> modelMapper.map(p, ProductDTO.class)).toList();
        if(products.isEmpty()){
            throw new APIException("Products not found with keyword: "+keyword);
        }

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(list);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElements(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product productFromDb = productRepository.findById(productId
        ).orElseThrow(()->new ResourceNotFoundexception("Product", "productId",productId));
        Product product=modelMapper.map(productDTO,Product.class);
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        Product savedProduct = productRepository.save(productFromDb);

        return modelMapper.map(savedProduct,ProductDTO.class);

    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new  ResourceNotFoundexception("Product","ProductId",productId));
       productRepository.delete(product);
       return modelMapper.map(product,ProductDTO.class);

    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundexception("Product","ProductId",productId));
        String fileName=fileService.uploadImage(path,image);
        productFromDb.setImage(fileName);
        Product updatedProduct=productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }


}
