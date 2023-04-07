package com.kodilla.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReturnCarDto {

    private Integer rentalId;
    private LocalDate returnDate;
    private double fuelLevel;
}
