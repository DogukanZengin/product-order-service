package com.dz.io.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.criterion.Order;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class SalesOrder{

    @Id
    @GeneratedValue
    private Long orderId;

    private String email;
    private BigDecimal totalPrice;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems;
}
