package com.kodilla.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CarGroupDto {

    private Integer carGroupId;
    private String name;
    private List<Integer> carsIds;
}
