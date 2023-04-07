package com.kodilla.carrental.mapper;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarGroupMapper {

    private final CarRepository carRepository;

    public CarGroup mapToCarGroup(CarGroupDto carGroupDto) {

        List<Car> cars = carGroupDto.getCarsIds().stream()
                .map(i -> carRepository.findById(i).get())
                .collect(Collectors.toList());

        return new CarGroup(
                carGroupDto.getCarGroupId(),
                carGroupDto.getName(),
                cars
        );
    }

    public CarGroupDto mapToCarGroupDto(CarGroup carGroup) {

        List<Integer> carsIds = carGroup.getCars().stream()
                .map(Car::getCarId)
                .collect(Collectors.toList());

        return new CarGroupDto(
                carGroup.getCarGroupId(),
                carGroup.getName(),
                carsIds
        );
    }

    public List<CarGroupDto> mapToCarGroupDtoList(List<CarGroup> carGroupList) {
        return carGroupList.stream()
                .map(this::mapToCarGroupDto)
                .collect(Collectors.toList());
    }
}
