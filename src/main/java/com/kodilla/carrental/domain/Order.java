package com.kodilla.carrental.domain;

import com.kodilla.carrental.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ORDER_ID", unique = true)
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name ="CLIENT_ID")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "RENTAL_ID")
    private Rental rental;

    @ManyToOne
    @JoinColumn(name ="CAR_ID")
    private Car car;

    @Column(name="DATE_FROM")
    private LocalDate dateFrom;

    @Column(name="DATE_TO")
    private LocalDate dateTo;

    @Column(name="STATUS")
    private OrderStatus status;

    @Column(name="CHILDSEAT")
    private boolean childSeat;

    @Column(name="GPS")
    private boolean gps;

    @Column(name="EXTRA_DRIVER")
    private boolean extraDriver;

    @Column(name="FUEL_LEVEL")
    private double fuelLevel;
}
