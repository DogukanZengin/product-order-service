package com.dz.io.service;

import com.dz.io.domain.OrderItem;
import com.dz.io.domain.Product;
import com.dz.io.domain.SalesOrder;
import com.dz.io.exception.ProductNotFoundException;
import com.dz.io.repository.OrderRepository;
import com.dz.io.repository.ProductRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    private OrderServiceImpl orderService;

    private SalesOrder order;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        orderService = new OrderServiceImpl(orderRepository, productRepository);
        order =  new SalesOrder();
        order.setOrderId(1L);
        order.setEmail("demo@demo.com");
        order.setCreationDate(LocalDateTime.now());

        Product product =  new Product();
        product.setProductId(1L);
        product.setName("DEMO");
        product.setPrice(new BigDecimal("5.0"));
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        order.setOrderItems(new ArrayList<>());
        order.getOrderItems().add(orderItem);
    }

    @Test
    public void whenPlaceOrderThenSuccess(){
        given(orderRepository.save(order)).willReturn(order);

        SalesOrder response = orderService.placeOrder(order);

        assertThat(response).isEqualTo(order);
        verify(orderRepository).save(order);
    }

    @Test
    public void whenRetrieveOrdersThenOrderListReturned(){
        given(orderRepository.findAllByCreationDateBetween(order.getCreationDate().minusMinutes(1),order.getCreationDate().plusMinutes(1)))
                .willReturn(Collections.singletonList(order));
        List<SalesOrder> orders = orderService.retrieveOrders(order.getCreationDate().minusMinutes(1),order.getCreationDate().plusMinutes(1));

        assertThat(orders).hasSize(1);
        assertThat(orders).containsExactly(order);
    }

    @Test
    public void whenOrderWithNonExistingProductThenException(){
        expectedException.expect(ProductNotFoundException.class);
        order.getOrderItems().get(0).getProduct().setProductId(1111L);
        orderService.placeOrder(order);
    }
}
