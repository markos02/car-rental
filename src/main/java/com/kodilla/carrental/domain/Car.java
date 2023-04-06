package com.kodilla.carrental.domain;

import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CARS")
public class Car {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "CAR_ID", unique = true)
    private Integer carId;

    @ManyToOne
    @JoinColumn(name = "CAR_GROUP_ID")
    private CarGroup carGroup;

    @OneToMany(
            targetEntity = Damage.class,
            mappedBy = "car",
            cascade = CascadeType.ALL
    )
    private List<Damage> damages = new ArrayList<>();

    @OneToMany(
            targetEntity = Order.class,
            mappedBy = "car",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Order> orders = new ArrayList<>();

    @Column(name = "LICENSE_PLATE")
    private String licensePlate;

    @Column(name = "FUEL_TYPE")
    private FuelType fuelType;

    @Column(name = "TRANSMISSION")
    private Transmission transmission;

    @Column(name = "MODEL")
    private String model;

    public Car(String licensePlate, FuelType fuelType, Transmission transmission, String model) {
        this.licensePlate = licensePlate;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.model = model;
    }
}
