package com.dz.io.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SalesOrderDto {

    private Long id;

    @NotNull(message = "{NotNull.email}")
    private String email;

    private LocalDateTime creationDate;

    List<OrderItemDto> orderItems;
}
