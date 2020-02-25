package com.dz.io.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDto {

    private Long productId;
    private String productName;
    private BigDecimal price;
}
