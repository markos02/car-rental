package com.kodilla.carrental.domain;

import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCarDto {

    private Integer carGroupId;
    private String licensePlate;
    private FuelType fuelType;
    private Transmission transmission;
    private String model;
}
