package com.dz.io.service;

import com.dz.io.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product createProduct(Product product);
    Product updateProduct(Product product, Long id);
    Product retrieveProduct(Long productId);
}
