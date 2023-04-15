package com.kodilla.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeeDto {
    private String description;
    private Double value;
}
