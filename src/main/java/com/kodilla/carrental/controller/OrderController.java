package com.kodilla.carrental.controller;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.exception.ClientNotFoundException;
import com.kodilla.carrental.exception.OrderNotFoundException;
import com.kodilla.carrental.mapper.OrderMapper;
import com.kodilla.carrental.mapper.RentalMapper;
import com.kodilla.carrental.service.OrderDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMapper orderMapper;
    private final OrderDbService orderDbService;
    private final RentalMapper rentalMapper;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<Order> orders = orderDbService.getAllOrders();
        return ResponseEntity.ok(orderMapper.mapToOrderDtoList(orders));
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Integer orderId) throws OrderNotFoundException {
        return ResponseEntity.ok(orderMapper.mapToOrderDto(orderDbService.getOrder(orderId)));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) throws ClientNotFoundException, CarNotFoundException {
        Order order = orderMapper.mapToOrder(orderDto);
        Order savedOrder = orderDbService.saveOrder(order);
        return ResponseEntity.ok(orderMapper.mapToOrderDto(savedOrder));
    }

    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) throws ClientNotFoundException, CarNotFoundException {
        Order order = orderMapper.mapToOrder(orderDto);
        Order savedOrder = orderDbService.saveOrder(order);
        return ResponseEntity.ok(orderMapper.mapToOrderDto(savedOrder));
    }

    @DeleteMapping(value = "/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) throws OrderNotFoundException {
        orderDbService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{orderId}")
    public ResponseEntity<RentalDto> startRental(@PathVariable Integer orderId) throws OrderNotFoundException {
        Rental rental = orderDbService.startRental(orderId);
        return ResponseEntity.ok(rentalMapper.mapToRentalDto(rental));
    }
}
