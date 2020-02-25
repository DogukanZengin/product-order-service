package com.dz.io.service;

import com.dz.io.domain.Product;
import com.dz.io.exception.ProductNotFoundException;
import com.dz.io.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product, Long id){
        if(retrieveProduct(id) == null){
            throw new ProductNotFoundException("Product does not exist in the catalogue : " + id);
        }
        product.setProductId(id);
        return productRepository.save(product);
    }

    @Override
    public Product retrieveProduct(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }
}
