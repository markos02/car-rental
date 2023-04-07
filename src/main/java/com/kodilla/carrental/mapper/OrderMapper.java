package com.kodilla.carrental.mapper;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.exception.ClientNotFoundException;
import com.kodilla.carrental.repository.CarRepository;
import com.kodilla.carrental.repository.ClientRepository;
import com.kodilla.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMapper {

    private final ClientRepository clientRepository;
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;

    public Order mapToOrder(OrderDto orderDto) throws ClientNotFoundException, CarNotFoundException {

        Client client = clientRepository.findById(orderDto.getClientId()).orElseThrow(ClientNotFoundException::new);
        Rental rental = rentalRepository.findById(orderDto.getRentalId()).orElse(null);
        Car car = carRepository.findById(orderDto.getCarId()).orElseThrow(CarNotFoundException::new);

        return new Order(
                orderDto.getOrderId(),
                client,
                rental,
                car,
                orderDto.getDateFrom(),
                orderDto.getDateTo(),
                orderDto.getStatus(),
                orderDto.isChildSeat(),
                orderDto.isGps(),
                orderDto.isExtraDriver(),
                orderDto.getFuelLevel()
        );
    }

    public OrderDto mapToOrderDto (Order order) {
        return new OrderDto(
                order.getOrderId(),
                order.getClient().getClientId(),
                order.getRental().getRentalId(),
                order.getCar().getCarId(),
                order.getDateFrom(),
                order.getDateTo(),
                order.getStatus(),
                order.isChildSeat(),
                order.isGps(),
                order.isExtraDriver(),
                order.getFuelLevel()
        );
    }

    public List<OrderDto> mapToOrderDtoList (List<Order> orderList) {
        return orderList.stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }
}
