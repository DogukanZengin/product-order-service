package com.dz.io.service;

import com.dz.io.domain.OrderItem;
import com.dz.io.domain.Product;
import com.dz.io.domain.SalesOrder;
import com.dz.io.exception.ProductNotFoundException;
import com.dz.io.repository.OrderRepository;
import com.dz.io.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService
{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(final OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public SalesOrder placeOrder(SalesOrder order) throws ProductNotFoundException {
        for(OrderItem item: order.getOrderItems()){
            Optional<Product> product = productRepository.findById(item.getProduct().getProductId());
            if(product.isEmpty()){
                throw new ProductNotFoundException("Product not found with id : " + item.getProduct().getProductId());
            }
            item.setPrice(product.get().getPrice());
        }
        order.getOrderItems().forEach(orderItem -> {orderItem.setOrderItemId(null);});
        order.setTotalPrice(order.getOrderItems().stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add));
        return orderRepository.save(order);
    }

    @Override
    public List<SalesOrder> retrieveOrders(LocalDateTime begin, LocalDateTime end) {
        return orderRepository.findAllByCreationDateBetween(begin, end);
    }
}
