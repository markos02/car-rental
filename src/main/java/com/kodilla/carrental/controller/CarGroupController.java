package com.kodilla.carrental.controller;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.mapper.CarGroupMapper;
import com.kodilla.carrental.mapper.CarMapper;
import com.kodilla.carrental.service.CarGroupDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/car_groups")
public class CarGroupController {

    private final CarGroupMapper carGroupMapper;
    private final CarGroupDbService carGroupDbService;
    private final CarMapper carMapper;

    @GetMapping
    public ResponseEntity<List<CarGroupDto>> getAllCarGroups() {
        List<CarGroup> carGroups = carGroupDbService.getAllCarGroups();
        return ResponseEntity.ok(carGroupMapper.mapToCarGroupDtoList(carGroups));
    }

    @GetMapping(value = "/{carGroupId}")
    public ResponseEntity<CarGroupDto> getCarGroup(@PathVariable Integer carGroupId) throws CarGroupNotFoundException {
        return ResponseEntity.ok(carGroupMapper.mapToCarGroupDto(carGroupDbService.getCarGroup(carGroupId)));
    }

    @PostMapping
    public ResponseEntity<CarGroupDto> createCarGroup(@RequestBody CarGroupDto carGroupDto) {
        CarGroup carGroup = carGroupMapper.mapToCarGroup(carGroupDto);
        CarGroup createdCarGroup = carGroupDbService.saveCarGroup(carGroup);
        return ResponseEntity.ok(carGroupMapper.mapToCarGroupDto(createdCarGroup));
    }

    @PutMapping
    public ResponseEntity<CarGroupDto> updateCarGroup(@RequestBody CarGroupDto carGroupDto) {
        CarGroup carGroup = carGroupMapper.mapToCarGroup(carGroupDto);
        CarGroup savedCarGroup = carGroupDbService.saveCarGroup(carGroup);
        return ResponseEntity.ok(carGroupMapper.mapToCarGroupDto(savedCarGroup));
    }

    @GetMapping(value = "/{carGroupId}/cars")
    public ResponseEntity<List<CarDto>> getAllCarsInGroup(@PathVariable Integer carGroupId) {
        List<Car> cars = carGroupDbService.getAllCarsInGroup(carGroupId);
        return ResponseEntity.ok(carMapper.mapToCarDtoList(cars));
    }

    @GetMapping(value = "/{carGroupId}/{dateFrom}/{dateTo}")
    public ResponseEntity<List<CarDto>> getAllAvailable(@PathVariable Integer carGroupId, @PathVariable LocalDate dateFrom, @PathVariable LocalDate dateTo) {
        List<Car> availableCars = carGroupDbService.getAllAvailableGroup(carGroupId, dateFrom, dateTo);
        return ResponseEntity.ok(carMapper.mapToCarDtoList(availableCars));
    }
}
