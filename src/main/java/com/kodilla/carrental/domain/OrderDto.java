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

    public static class OrderDtoBuilder {
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

        public OrderDtoBuilder orderId(Integer orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderDtoBuilder clientId(Integer clientId) {
            this.clientId = clientId;
            return this;
        }

        public OrderDtoBuilder rentalId(Integer rentalId) {
            this.rentalId = rentalId;
            return this;
        }

        public OrderDtoBuilder carId(Integer carId) {
            this.carId = carId;
            return this;
        }

        public OrderDtoBuilder dateFrom(LocalDate dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public OrderDtoBuilder dateTo(LocalDate dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public OrderDtoBuilder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderDtoBuilder childSeat(boolean childSeat) {
            this.childSeat = childSeat;
            return this;
        }

        public OrderDtoBuilder gps(boolean gps) {
            this.gps = gps;
            return this;
        }

        public OrderDtoBuilder extraDriver(boolean extraDriver) {
            this.extraDriver = extraDriver;
            return this;
        }

        public OrderDtoBuilder fuelLevel(double fuelLevel) {
            this.fuelLevel = fuelLevel;
            return this;
        }

        public OrderDto build() {
            return new OrderDto(orderId, clientId, rentalId, carId, dateFrom, dateTo, status, childSeat, gps, extraDriver, fuelLevel);
        }
    }
}
