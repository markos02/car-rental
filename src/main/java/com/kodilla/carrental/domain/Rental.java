package com.kodilla.carrental.domain;

import com.kodilla.carrental.domain.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@NamedNativeQuery(
        name = "Rental.retrieveRentalsBetweenDates",
        query = "SELECT O.* FROM RENTALS R JOIN ORDERS O ON R.ORDER_ID = O.ORDER_ID" +
                " WHERE (DATEDIFF(:RENTAL_START, O.DATE_FROM) >= 0" +
                " AND DATEDIFF(:RENTAL_START, O.DATE_TO) <= 0)" +
                " OR (DATEDIFF(:RENTAL_END, O.DATE_FROM) >= 0" +
                " AND DATEDIFF(:RENTAL_END, O.DATE_TO) <= 0)" +
                " OR (DATEDIFF(:RENTAL_START, O.DATE_FROM) <= 0" +
                " AND DATEDIFF(:RENTAL_END, O.DATE_TO) >= 0)",
        resultClass = Rental.class
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RENTALS")
public class Rental {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "RENTAL_ID", unique = true)
    private Integer rentalId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @OneToMany(
            targetEntity = Damage.class,
            mappedBy = "rental",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Damage> damages = new ArrayList<>();

    @Column(name = "STATUS")
    private RentalStatus status;
}
