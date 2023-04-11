package com.kodilla.carrental.controller;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.CarDto;
import com.kodilla.carrental.domain.CreateCarDto;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.mapper.CarMapper;
import com.kodilla.carrental.service.CarDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarMapper carMapper;
    private final CarDbService carDbService;

    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        List<Car> cars = carDbService.getAllCars();
        return ResponseEntity.ok(carMapper.mapToCarDtoList(cars));
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarDto> getCar(@PathVariable Integer carId) throws CarNotFoundException {
        return ResponseEntity.ok(carMapper.mapToCarDto(carDbService.getCar(carId)));
    }

    @PostMapping
    public ResponseEntity<CarDto> createCar(@RequestBody CreateCarDto createCarDto) throws CarGroupNotFoundException {
        Car car = carMapper.mapToCreateCar(createCarDto);
        Car createdCar = carDbService.createNewCar(car, createCarDto.getCarGroupId());
        return ResponseEntity.ok(carMapper.mapToCarDto(createdCar));
    }

    @PutMapping
    public ResponseEntity<CarDto> updateCar(@RequestBody CarDto carDto) throws CarGroupNotFoundException {
        Car car = carMapper.mapToCar(carDto);
        Car savedCar = carDbService.saveCar(car);
        return ResponseEntity.ok(carMapper.mapToCarDto(savedCar));
    }

    @GetMapping(value="/isAvailable/{carId}/{dateFrom}/{dateTo}")
    public ResponseEntity<Object> checkIfAvailable(@PathVariable Integer carId, @PathVariable LocalDate dateFrom, @PathVariable LocalDate dateTo) {
        if (carDbService.checkIfAvailable(carId, dateFrom, dateTo)) {
            return new ResponseEntity<>("Hurray! Car with given ID is available in given period", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unfortunately, car with given ID is not available in given period", HttpStatus.OK);
        }
    }

    @GetMapping(value = "/allAvailable/{dateFrom}/{dateTo}")
    public ResponseEntity<List<CarDto>> getAllAvailable(@PathVariable LocalDate dateFrom, @PathVariable LocalDate dateTo) {
        System.out.println(dateFrom + " " + dateTo);
        List<Car> availableCars = carDbService.getAllAvailable(dateFrom, dateTo);
        return ResponseEntity.ok(carMapper.mapToCarDtoList(availableCars));
    }
}
