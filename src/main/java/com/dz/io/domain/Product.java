package com.dz.io.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public final class Product {

    @Id
    @GeneratedValue
    private Long productId;
    private String name;
    private BigDecimal price;
}
