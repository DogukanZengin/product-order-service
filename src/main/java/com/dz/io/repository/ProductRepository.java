package com.dz.io.repository;

import com.dz.io.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Override
    List<Product> findAll();
}
