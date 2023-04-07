package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.enums.RentalStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RentalRepositoryTestSuite {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DamageRepository damageRepository;

    @Test
    void testSaveEmptyRental() {
        //Given
        Rental rental = new Rental();

        //When
        rentalRepository.save(rental);
        int id = rental.getRentalId();
        Optional<Rental> readRental = rentalRepository.findById(id);

        //Then
        assertTrue(readRental.isPresent());

        //CleanUp
        rentalRepository.deleteById(id);
    }

    @Test
    void testSaveRentalWithStatus() {
        //Given
        Rental rental = new Rental();
        rental.setStatus(RentalStatus.ORDERED);

        //When
        rentalRepository.save(rental);
        int id = rental.getRentalId();
        Optional<Rental> readRental = rentalRepository.findById(id);

        //Then
        assertTrue(readRental.isPresent());
        assertEquals(RentalStatus.ORDERED, readRental.get().getStatus());

        //CleanUp
        rentalRepository.deleteById(id);
    }

    @Test
    void testSaveRentalWithOrder() {
        //Given
        Rental rental = new Rental();
        rental.setStatus(RentalStatus.ORDERED);
        Order order = new Order();
        order.setRental(rental);
        rental.setOrder(order);

        //When
        rentalRepository.save(rental);
        int id = rental.getRentalId();
        Optional<Rental> readRental = rentalRepository.findById(id);

        //Then
        assertTrue(readRental.isPresent());
        assertEquals(RentalStatus.ORDERED, readRental.get().getStatus());
        assertNotNull(readRental.get().getOrder());
        assertEquals(1, orderRepository.findAll().size());

        //CleanUp
        rentalRepository.deleteById(id);
    }

    @Test
    void testSaveRentalWithDamages() {
        //Given
        Rental rental = new Rental();
        rental.setStatus(RentalStatus.ORDERED);
        Order order = new Order();
        rental.setOrder(order);

        Damage damage1 = new Damage();
        damage1.setRental(rental);
        Damage damage2 = new Damage();
        damage2.setRental(rental);
        Damage damage3 = new Damage();
        damage3.setRental(rental);

        rental.getDamages().add(damage1);
        rental.getDamages().add(damage2);
        rental.getDamages().add(damage3);

        //When
        rentalRepository.save(rental);
        int id = rental.getRentalId();
        Optional<Rental> readRental = rentalRepository.findById(id);
        List<Damage> readDamages = damageRepository.findAll();

        //Then
        assertTrue(readRental.isPresent());
        assertEquals(3, readDamages.size());

        //CleanUp
        rentalRepository.deleteById(id);
    }

    @Test
    void testRetrieveRentalsBetweenDates() {
        //Given
        LocalDate rental1DateFrom = LocalDate.of(2023,2,5);
        LocalDate rental1DateTo = LocalDate.of(2023,2,12);
        LocalDate rental2DateFrom = LocalDate.of(2023,2,10);
        LocalDate rental2DateTo = LocalDate.of(2023,2,20);

        Order order1 = new Order();
        order1.setDateFrom(rental1DateFrom);
        order1.setDateTo(rental1DateTo);
        Order order2= new Order();
        order2.setDateFrom(rental2DateFrom);
        order2.setDateTo(rental2DateTo);

        Rental rental1 = new Rental();
        rental1.setOrder(order1);
        Rental rental2 = new Rental();
        rental2.setOrder(order2);

        order1.setRental(rental1);
        order2.setRental(rental2);

        //When
        rentalRepository.save(rental1);
        int id1 = rental1.getRentalId();
        rentalRepository.save(rental2);
        int id2 = rental2.getRentalId();

        List<Rental> readRentalsNoRentals1 = rentalRepository.retrieveRentalsBetweenDates(rental1DateFrom.minusDays(10), rental1DateFrom.minusDays(2));
        List<Rental> readRentalsNoRentals2 = rentalRepository.retrieveRentalsBetweenDates(rental2DateTo.plusDays(1), rental2DateTo.plusDays(2));
        List<Rental> readRentalsOneRental1 = rentalRepository.retrieveRentalsBetweenDates(rental1DateFrom.minusDays(1), rental1DateFrom.plusDays(2));
        List<Rental> readRentalsOneRental2 = rentalRepository.retrieveRentalsBetweenDates(rental2DateFrom.plusDays(3), rental2DateFrom.plusDays(10));
        List<Rental> readRentalsTwoRentals = rentalRepository.retrieveRentalsBetweenDates(rental1DateFrom.plusDays(3), rental1DateFrom.plusDays(6));

        //Then
        assertEquals(0,readRentalsNoRentals1.size());
        assertEquals(0,readRentalsNoRentals2.size());
        assertEquals(1,readRentalsOneRental1.size());
        assertEquals(1,readRentalsOneRental2.size());
        assertEquals(2,readRentalsTwoRentals.size());

        //CleanUp
        rentalRepository.deleteById(id1);
        rentalRepository.deleteById(id2);
    }
}
