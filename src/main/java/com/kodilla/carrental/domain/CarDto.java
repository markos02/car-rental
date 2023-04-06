package com.kodilla.carrental.domain;

import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CarDto {

    private Integer carId;
    private Integer carGroupId;
    private List<Integer> damagesIds;
    private List<Integer> ordersIds;
    private String licensePlate;
    private FuelType fuelType;
    private Transmission transmission;
    private String model;

}
