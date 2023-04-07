package com.kodilla.carrental.domain;

import com.kodilla.carrental.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class OrderDto {

    private Integer orderId;
    private Integer clientId;
    private Integer rentalId;
    private Integer carId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private OrderStatus status;
    private boolean childSeat;
    private boolean gps;
    private boolean extraDriver;
    private double fuelLevel;
}
