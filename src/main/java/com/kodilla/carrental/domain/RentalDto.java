package com.kodilla.carrental.domain;

import com.kodilla.carrental.domain.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RentalDto {

    private Integer rentalId;
    private Integer orderId;
    private List<Integer> damagesIds;
    private RentalStatus status;
}
