package com.dz.io.service;

import com.dz.io.domain.SalesOrder;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    SalesOrder placeOrder(SalesOrder order);
    List<SalesOrder> retrieveOrders(LocalDateTime begin, LocalDateTime end);
}
