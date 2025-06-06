package com.rudraprogram.product_service.service;

import com.rudraprogram.product_service.dto.ProductRequest;
import com.rudraprogram.product_service.dto.ProductResponse;
import com.rudraprogram.product_service.model.Product;
import com.rudraprogram.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {


    private final  ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
    Product product = Product.builder()
            .name(productRequest.getName())
            .description(productRequest.getDescription())
            .price(productRequest.getPrice())
            .build();
    productRepository.save(product);
    log.info("Product {} is saved",product.getId());

    }

    public List<ProductResponse> getAllProducts() {
       List<Product> products = productRepository.findAll();
       return  products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return  ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
