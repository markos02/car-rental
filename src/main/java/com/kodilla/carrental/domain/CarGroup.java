package com.kodilla.carrental.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CAR_GROUPS")
public class CarGroup {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "CAR_GROUP_ID", unique = true)
    private Integer carGroupId;

    @Column(name="NAME")
    private String name;

    @OneToMany(
            targetEntity = Car.class,
            mappedBy = "carGroup",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Car> cars;

    public CarGroup(String name) {
        this.name = name;
        this.cars = new ArrayList<>();
    }
}
