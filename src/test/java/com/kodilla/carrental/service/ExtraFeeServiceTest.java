package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.Fee;
import com.kodilla.carrental.domain.Rental;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExtraFeeServiceTest {

    @Autowired
    ExtraFeeService extraFeeService;

    @Test
    void TestNewDamagesFee() {
        //Given
        List<Damage> damages = List.of(
                new Damage(1, LocalDate.of(2023, 04, 11), new Car(), new Rental(), "Broken lamp"),
                new Damage(2, LocalDate.of(2023, 04, 11), new Car(), new Rental(), "Flat tire")
        );

        //When
        Fee fee = extraFeeService.newDamagesFee(damages);

        //Then
        assertEquals("Damage fee", fee.getDescription());
        assertEquals(1000.0, fee.getValue());
    }

    @Test
    void TestOverdueFee() {
        //Given
        //When
        Fee fee = extraFeeService.overdueFee(2L);

        //Then
        assertEquals("Overdue fee", fee.getDescription());
        assertEquals(200, fee.getValue());
    }

    @Test
    void TestFuelFee() {
        //Given
        //When
        Fee fee = extraFeeService.fuelFee(3.5);

        //Then
        assertEquals("Fuel fee", fee.getDescription());
        assertNotNull(fee.getValue());
    }
}