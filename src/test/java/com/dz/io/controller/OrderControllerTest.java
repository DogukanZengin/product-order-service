package com.dz.io.controller;

import com.dz.io.domain.OrderItem;
import com.dz.io.domain.Product;
import com.dz.io.domain.SalesOrder;
import com.dz.io.dto.OrderItemDto;
import com.dz.io.dto.SalesOrderDto;
import com.dz.io.exception.ProductNotFoundException;
import com.dz.io.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<SalesOrderDto> json;
    private JacksonTester<List<SalesOrder>> jsonList;

    private SalesOrderDto order;


    @Before
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
        order =  new SalesOrderDto();
        order.setId(1L);
        order.setEmail("demo@demo.com");
        order.setCreationDate(LocalDateTime.now());

        Product product =  new Product();
        product.setProductId(1L);
        product.setName("DEMO");
        product.setPrice(new BigDecimal("5.0"));
        OrderItemDto orderItem = new OrderItemDto();
        orderItem.setProductId(1L);
        order.setOrderItems(new ArrayList<>());
        order.getOrderItems().add(orderItem);
    }

    @Test
    public void whenRetrieveOrdersThenSuccess() throws Exception {
        List<SalesOrder> orders = Stream.generate(SalesOrder::new).limit(5).collect(Collectors.toList());
        long counter= 1;
        Product product = new Product();
        product.setProductId(counter);
        product.setPrice(new BigDecimal("5.0"));
        product.setName("DEMO");
        for (SalesOrder order : orders) {
            order.setOrderId(counter);
            order.setEmail("demo@demo.com");
            order.setCreationDate(LocalDateTime.now());
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            order.setOrderItems(new ArrayList<>());
            order.getOrderItems().add(orderItem);
        }
        LocalDateTime now = LocalDateTime.now();
        given(orderService.retrieveOrders(now.minusMinutes(10), now.plusMinutes(10))).willReturn(orders);

        MockHttpServletResponse response = mvc.perform(get("/orders")
                .param("start",now.minusMinutes(10).toString())
                .param("end", now.plusMinutes(10).toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Success"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(orderService).retrieveOrders(now.minusMinutes(10), now.plusMinutes(10));
    }

    @Test
    public void whenPlaceOrderThenSuccess() throws Exception {
        String orderJson = "{\"email\":\"demo@demo.com\",\"orderItems\":[{\"productId\":1}]}";
        SalesOrder salesOrder =  new SalesOrder();
        salesOrder.setOrderId(1L);
        given(orderService.placeOrder(modelMapper.map(order,SalesOrder.class))).willReturn(salesOrder);
        MockHttpServletResponse response = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void whenOrderWithNonExistingProductThenError() throws Exception{
        String orderJson = "{\"email\":\"demo@demo.com\",\"orderItems\":[{\"productId\":11111}]}";
        given(orderService.placeOrder(modelMapper.map(order,SalesOrder.class))).willThrow(ProductNotFoundException.class);
        MockHttpServletResponse response = mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
