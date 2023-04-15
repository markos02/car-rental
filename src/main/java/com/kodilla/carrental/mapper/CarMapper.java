package com.kodilla.carrental.mapper;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.repository.CarGroupRepository;
import com.kodilla.carrental.repository.DamageRepository;
import com.kodilla.carrental.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarMapper {

    private final CarGroupRepository carGroupRepository;
    private final DamageRepository damageRepository;
    private final OrderRepository orderRepository;

    public Car mapToCar(CarDto carDto) throws CarGroupNotFoundException {

        List<Damage> damages = carDto.getDamagesIds().stream()
                .map(i -> damageRepository.findById(i).get())
                .collect(Collectors.toList());

        List<Order> orders = carDto.getOrdersIds().stream()
                .map(i -> orderRepository.findById(i).get())
                .collect(Collectors.toList());

        return new Car(
                carDto.getCarId(),
                carGroupRepository.findById(carDto.getCarGroupId()).orElseThrow(CarGroupNotFoundException::new),
                damages,
                orders,
                carDto.getLicensePlate(),
                carDto.getFuelType(),
                carDto.getTransmission(),
                carDto.getModel()
        );
    }

    public CarDto mapToCarDto(Car car) {

        List<Integer> damagesIds = car.getDamages().stream()
                .map(Damage::getDamageId)
                .collect(Collectors.toList());

        List<Integer> ordersIds = car.getOrders().stream()
                .map(Order::getOrderId)
                .collect(Collectors.toList());

        return new CarDto(
                car.getCarId(),
                car.getCarGroup().getCarGroupId(),
                damagesIds,
                ordersIds,
                car.getLicensePlate(),
                car.getFuelType(),
                car.getTransmission(),
                car.getModel()
        );
    }

    public List<CarDto> mapToCarDtoList(List<Car> carList) {
        return carList.stream()
                .map(this::mapToCarDto)
                .collect(Collectors.toList());
    }

    public Car mapToCreateCar(CreateCarDto createCarDto) {
        return new Car(
                createCarDto.getLicensePlate(),
                createCarDto.getFuelType(),
                createCarDto.getTransmission(),
                createCarDto.getModel()
        );
    }
}
