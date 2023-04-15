package com.kodilla.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DamageDto {

    private Integer damageId;
    private LocalDate date;
    private Integer carId;
    private Integer rentalId;
    private String description;
}
