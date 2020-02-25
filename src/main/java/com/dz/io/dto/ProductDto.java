package com.dz.io.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {

    private Long productId;

    @NotNull(message = "{NotNull.name}")
    private String name;

    @Min(1)
    @NotNull(message = "{NotNull.price}")
    private BigDecimal price;

}
