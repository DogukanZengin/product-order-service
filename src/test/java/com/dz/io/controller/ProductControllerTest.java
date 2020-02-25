package com.dz.io.controller;

import com.dz.io.domain.Product;
import com.dz.io.domain.SalesOrder;
import com.dz.io.dto.ProductDto;
import com.dz.io.exception.ProductNotFoundException;
import com.dz.io.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<ProductDto> json;
    private JacksonTester<List<Product>> jsonList;

    private ProductDto product;
    private Product entity;

    @Before
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
        product =  new ProductDto();
        product.setProductId(1L);
        product.setPrice(new BigDecimal("5.0"));
        product.setName("DEMO");

        entity =  new Product();
        entity.setProductId(1L);
        entity.setPrice(new BigDecimal("5.0"));
        entity.setName("DEMO");
    }

    @Test
    public void whenGetProductsThenSuccess() throws Exception {
        List<Product> products = Stream.generate(Product::new).limit(2).collect(Collectors.toList());
        long counter= 1;
        for (Product product : products) {
            product.setProductId(counter);
            product.setPrice(new BigDecimal("5.0"));
            product.setName("DEMO-" + counter++);
        }
        given(productService.getAllProducts()).willReturn(products);

        MockHttpServletResponse response = mvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").value("Success"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(productService).getAllProducts();
    }

    @Test
    public void whenCreateProductThenSuccess() throws Exception {
        given(productService.createProduct(entity)).willReturn(entity);

        MockHttpServletResponse response = mvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                .content(json.write(product).getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void whenCreateProductWithoutNameThenError() throws Exception {
        product.setName(null);
        given(productService.createProduct(entity)).willReturn(entity);

        MockHttpServletResponse response = mvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                .content(json.write(product).getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenUpdateProductThenSuccess() throws Exception {
        product.setName("UPDATED_DEMO");
        given(productService.updateProduct(entity,1L)).willReturn(entity);

        MockHttpServletResponse response = mvc.perform(put("/products/1").contentType(MediaType.APPLICATION_JSON)
                .content(json.write(product).getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Test
    public void whenUpdateNonExistingProductThanError() throws Exception {
        product.setProductId(null);
        given(productService.updateProduct(modelMapper.map(product, Product.class),1111L)).willThrow(ProductNotFoundException.class);
        MockHttpServletResponse response = mvc.perform(put("/products/1111").contentType(MediaType.APPLICATION_JSON)
                .content(json.write(product).getJson())).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
