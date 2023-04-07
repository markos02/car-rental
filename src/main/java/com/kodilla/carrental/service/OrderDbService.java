package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.enums.OrderStatus;
import com.kodilla.carrental.domain.enums.RentalStatus;
import com.kodilla.carrental.exception.OrderNotFoundException;
import com.kodilla.carrental.repository.OrderRepository;
import com.kodilla.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDbService {

    private final OrderRepository orderRepository;
    private final RentalRepository rentalRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(Integer orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Integer orderId) throws OrderNotFoundException {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new OrderNotFoundException();
        }
    }

    public Rental startRental(Integer orderId) throws OrderNotFoundException {
        Order order = getOrder(orderId);
        order.setStatus(OrderStatus.COMPLETED);
        saveOrder(order);

        Rental rental = new Rental();
        rental.setOrder(order);
        rental.setStatus(RentalStatus.ORDERED);
        return rentalRepository.save(rental);
    }
}
