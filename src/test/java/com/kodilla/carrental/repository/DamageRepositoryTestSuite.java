package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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
        damageRepository.deleteById(id);
        carRepository.deleteById(carId);
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

    @Test
    void testFindDamagesByCar() {
        //Given
        String description = "Description of test damage";
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");
        Car car2 = new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2");

        Damage damage1 = new Damage();
        damage1.setDescription(description + " 1");
        damage1.setCar(car1);

        Damage damage2 = new Damage();
        damage2.setDescription(description + " 2");
        damage2.setCar(car1);

        car1.getDamages().add(damage1);
        car1.getDamages().add(damage2);

        //When
        carRepository.save(car1);
        carRepository.save(car2);
        Integer carId1 = car1.getCarId();
        Integer carId2 = car2.getCarId();

        damageRepository.save(damage1);
        damageRepository.save(damage2);

        List<Damage> damageList1 = damageRepository.findDamagesByCar_CarId(carId1);
        List<Damage> damageList2 = damageRepository.findDamagesByCar_CarId(carId2);

        //Then
        assertEquals(2, damageList1.size());
        assertEquals(0, damageList2.size());

        //CleanUp
        carRepository.deleteById(carId1);
        carRepository.deleteById(carId2);
    }

    @Test
    @Transactional
    void testFindDamagesByRental() {
        //Given
        String description = "Description of test damage";
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");
        Car car2 = new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2");

        Damage damage1 = new Damage();
        damage1.setDescription(description + " 1");
        damage1.setCar(car1);

        Damage damage2 = new Damage();
        damage2.setDescription(description + " 2");
        damage2.setCar(car1);

        car1.getDamages().add(damage1);
        car1.getDamages().add(damage2);

        Rental rental1 = new Rental();
        Rental rental2 = new Rental();

        rental1.getDamages().add(damage1);
        rental1.getDamages().add(damage2);
        damage1.setRental(rental1);
        damage2.setRental(rental1);

        //When
        carRepository.save(car1);
        carRepository.save(car2);
        Integer carId1 = car1.getCarId();
        Integer carId2 = car2.getCarId();

        damageRepository.save(damage1);
        damageRepository.save(damage2);

        rentalRepository.save(rental1);
        rentalRepository.save(rental2);
        Integer rentalId1 = rental1.getRentalId();
        Integer rentalId2 = rental2.getRentalId();

        List<Damage> damageList1 = damageRepository.findDamagesByRental_RentalId(rentalId1);
        List<Damage> damageList2 = damageRepository.findDamagesByRental_RentalId(rentalId2);

        //Then
        assertNotNull(damageList1);
        assertNotNull(damageList2);
        assertEquals(2, damageList1.size());
        assertEquals(0, damageList2.size());

        //CleanUp
        carRepository.deleteById(carId1);
        carRepository.deleteById(carId2);
        rentalRepository.deleteById(rentalId1);
        rentalRepository.deleteById(rentalId2);
    }
}