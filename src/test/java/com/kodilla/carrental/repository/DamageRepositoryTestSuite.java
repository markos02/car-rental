package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.Rental;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DamageRepositoryTestSuite {

    @Autowired
    DamageRepository damageRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Test
    void testSaveEmptyDamage() {
        //Given
        Damage damage = new Damage();

        //When
        damageRepository.save(damage);
        int id = damage.getDamageId();
        Optional<Damage> readDamage = damageRepository.findById(id);

        //Then
        assertTrue(readDamage.isPresent());

        //CleanUp
        damageRepository.deleteById(id);
    }

    @Test
    void testSaveDamageWithDescription() {

        String description = "Description of test damage";

        //Given
        Damage damage = new Damage();
        damage.setDescription(description);

        //When
        damageRepository.save(damage);
        int id = damage.getDamageId();
        Optional<Damage> readDamage = damageRepository.findById(id);

        //Then
        assertTrue(readDamage.isPresent());
        assertEquals(description, readDamage.get().getDescription());

        //CleanUp
        damageRepository.deleteById(id);
    }

    @Transactional
    @Test
    void testSaveDamageWithCar() {

        String description = "Description of test damage";

        //Given
        Car car = new Car();
        carRepository.save(car);
        int carId = car.getCarId();

        Damage damage = new Damage();
        damage.setDescription(description);
        damage.setCar(car);

        //When
        damageRepository.save(damage);
        int id = damage.getDamageId();
        Optional<Damage> readDamage = damageRepository.findById(id);
        Car readCar = readDamage.get().getCar();

        //Then
        assertTrue(readDamage.isPresent());
        assertNotNull(readCar);

        //CleanUp
        //damageRepository.deleteById(id);
        //carRepository.deleteById(carId);
    }

    @Test
    void testSaveDamageWithRental() {

        String description = "Description of test damage";

        //Given
        Rental rental = new Rental();
        rentalRepository.save(rental);
        int rentalId = rental.getRentalId();

        Damage damage = new Damage();
        damage.setDescription(description);
        damage.setRental(rental);

        //When
        damageRepository.save(damage);
        int id = damage.getDamageId();
        Optional<Damage> readDamage = damageRepository.findById(id);

        //Then
        assertTrue(readDamage.isPresent());
        assertNotNull(readDamage.get().getRental());

        //CleanUp
        damageRepository.deleteById(id);
        rentalRepository.deleteById(rentalId);
    }
}
