package com.kodilla.carrental.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "DAMAGES")
public class Damage {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "DAMAGE_ID", unique = true)
    private Integer damageId;
    private Date date;

    @ManyToOne
    @JoinColumn(name ="CAR_ID")
    private Car car;

    @ManyToOne
    @JoinColumn(name ="RENTAL_ID")
    private Rental rental;

    @Column(name="DESCRIPTION")
    private String description;
}
