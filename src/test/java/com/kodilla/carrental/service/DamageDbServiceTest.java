package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.exception.DamageNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DamageDbServiceTest {

    @Autowired
    DamageDbService damageDbService;

    @Autowired
    CarDbService carDbService;

    @Test
    void testGetCarDamages() throws CarNotFoundException {
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
        carDbService.saveCar(car1);
        carDbService.saveCar(car2);
        Integer carId1 = car1.getCarId();
        Integer carId2 = car2.getCarId();

        List<Damage> damageList1 = damageDbService.getCarDamages(carId1);
        List<Damage> damageList2 = damageDbService.getCarDamages(carId2);

        //Then
        assertEquals(2,damageList1.size());
        assertEquals(0,damageList2.size());

        //CleanUp
        carDbService.deleteCar(carId1);
        carDbService.deleteCar(carId2);
    }

    @Test
    void testDeleteDamage() throws CarNotFoundException {
        //Given
        String description = "Description of test damage";
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        Damage damage1 = new Damage();
        damage1.setDescription(description);
        damage1.setCar(car1);

        car1.getDamages().add(damage1);

        //When
        carDbService.saveCar(car1);
        Integer carId1 = car1.getCarId();
        Integer damageId1 = damage1.getDamageId();

        //Then
        assertDoesNotThrow(()->damageDbService.deleteDamage(damageId1));
        assertThrows(DamageNotFoundException.class, ()->damageDbService.deleteDamage(damageId1));

        //CleanUp
        carDbService.deleteCar(carId1);
    }

}