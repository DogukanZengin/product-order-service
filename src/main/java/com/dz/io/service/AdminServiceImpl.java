package com.dz.io.service;

import com.dz.io.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final ProductRepository productRepository;

    @Autowired
    public AdminServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void deleteDatabaseContents() {
        productRepository.deleteAll();
    }
}
