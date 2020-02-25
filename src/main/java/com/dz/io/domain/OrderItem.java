package com.dz.io.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private Long orderItemId;

    @ManyToOne
    private Product product;

    private BigDecimal price;
}
