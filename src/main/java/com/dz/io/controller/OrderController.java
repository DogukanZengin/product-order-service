package com.dz.io.controller;


import com.dz.io.domain.SalesOrder;
import com.dz.io.dto.ResponseDto;
import com.dz.io.dto.SalesOrderDto;
import com.dz.io.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@Api(tags="Order API")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(final OrderService orderService,
                           final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }

    @PostMapping
    @ApiOperation(value = "${api.docs.orders.place}")
    ResponseEntity<ResponseDto<?>>  placeOrder(@RequestBody SalesOrderDto salesOrderDto){
        SalesOrderDto dto = convertToDto(orderService.placeOrder(convertToEntity(salesOrderDto)));
        ResponseDto<?> response = ResponseDto.builder()
                .status(HttpStatus.CREATED.toString())
                .body(dto).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @ApiOperation(value = "${api.docs.orders.retrieve}")
    ResponseEntity<ResponseDto<?>> retrieveOrders(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                  @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
        List<SalesOrderDto> orders = orderService.retrieveOrders(start,end).stream().map(this::convertToDto).collect(Collectors.toList());
        ResponseDto<?> response = ResponseDto.builder()
                .status(HttpStatus.OK.toString())
                .body(orders).build();
        return ResponseEntity.ok(response);
    }

    private SalesOrderDto convertToDto(SalesOrder order) {
        return modelMapper.map(order, SalesOrderDto.class);
    }

    private SalesOrder convertToEntity(SalesOrderDto salesOrderDto){
        return modelMapper.map(salesOrderDto, SalesOrder.class);
    }
}
