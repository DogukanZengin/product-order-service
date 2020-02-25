package com.dz.io.service;

import com.dz.io.domain.Product;
import com.dz.io.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    private ProductServiceImpl productServiceImpl;

    private Product product;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        productServiceImpl = new ProductServiceImpl(productRepository);
        product =  new Product();
        product.setProductId(1L);
        product.setPrice(new BigDecimal("5.0"));
        product.setName("DEMO");
    }

    @Test
    public void getProducts(){
        List<Product> products =  new ArrayList<>();
        products.add(product);

        given(productServiceImpl.getAllProducts()).willReturn(products);

        List<Product> response = productRepository.findAll();

        assertThat(response).hasSize(1);
        assertThat(response).containsExactly(product);
    }

    @Test
    public void whenCreateProductThenSuccess(){
        given(productRepository.save(product)).willReturn(product);

        Product response = productServiceImpl.createProduct(product);
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isNotNull();
    }

    @Test
    public void whenUpdateProductThenSuccess(){
        product.setPrice(new BigDecimal("6.0"));
        given(productRepository.save(product)).willReturn(product);
        Product response = productServiceImpl.updateProduct(product,1L);
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("6.0"));
    }

}
