package com.dz.io.controller;


import com.dz.io.domain.Product;
import com.dz.io.dto.ProductDto;
import com.dz.io.dto.ResponseDto;
import com.dz.io.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@Api(tags="Product API")
public final class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(final ProductService productService, final ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ApiOperation(value = "${api.docs.products.retrieve}")
    ResponseEntity<ResponseDto<?>> retrieveProducts(){
        List<ProductDto> products = productService.getAllProducts().stream().map(this::convertToDto).collect(Collectors.toList());
        ResponseDto<?> response = ResponseDto.builder()
                .status(HttpStatus.OK.toString())
                .body(products).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @ApiOperation(value = "${api.docs.products.create}")
    ResponseEntity<ResponseDto<?>> createProduct(@RequestBody @Valid ProductDto productDto){
        ResponseDto<?> response = ResponseDto.builder()
                .status(HttpStatus.CREATED.toString())
                .body(productService.createProduct(convertToEntity(productDto))).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{productId}")
    @ApiOperation(value = "${api.docs.products.update}")
    ResponseEntity<ResponseDto<?>> updateProduct(final @PathVariable Long productId, @RequestBody ProductDto productDto){
        ResponseDto<?> response = ResponseDto.builder()
                .status(HttpStatus.ACCEPTED.toString())
                .body(productService.updateProduct(convertToEntity(productDto),productId)).build();
        return ResponseEntity.accepted().body(response);
    }

    private ProductDto convertToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    private Product convertToEntity(ProductDto productDto){
        return modelMapper.map(productDto, Product.class);
    }
}
