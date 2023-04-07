package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.CarGroup;
import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.repository.RentalRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarGroupDbServiceTest {

    @Autowired
    CarGroupDbService carGroupDbService;

    @Autowired
    CarDbService carDbService;

    @Autowired
    RentalRepository rentalRepository;

    @Test
    void testGetAllCarGroups() throws CarGroupNotFoundException {
        //Given
        CarGroup carGroup1 = new CarGroup("Economy");
        CarGroup carGroup2 = new CarGroup("Regular");
        CarGroup carGroup3 = new CarGroup("Premium");

        //When
        carGroupDbService.saveCarGroup(carGroup1);
        carGroupDbService.saveCarGroup(carGroup2);
        carGroupDbService.saveCarGroup(carGroup3);

        Integer carGroupId1 = carGroup1.getCarGroupId();
        Integer carGroupId2 = carGroup2.getCarGroupId();
        Integer carGroupId3 = carGroup3.getCarGroupId();

        //Then
        assertEquals(3, carGroupDbService.getAllCarGroups().size());

        //CleanUp
        carGroupDbService.deleteCarGroup(carGroupId1);
        carGroupDbService.deleteCarGroup(carGroupId2);
        carGroupDbService.deleteCarGroup(carGroupId3);
    }

    @Test
    void testGetCarGroup() throws CarGroupNotFoundException {
        //Given
        CarGroup carGroup1 = new CarGroup("Economy");

        //When
        carGroupDbService.saveCarGroup(carGroup1);
        Integer carGroupId1 = carGroup1.getCarGroupId();
        CarGroup retrievedCarGroup = carGroupDbService.getCarGroup(carGroupId1);

        //Then
        assertNotNull(retrievedCarGroup);
        assertEquals(carGroupId1, retrievedCarGroup.getCarGroupId());
        assertEquals(carGroup1.getName(), retrievedCarGroup.getName());
        assertThrows(CarGroupNotFoundException.class, () -> carGroupDbService.getCarGroup(carGroupId1 - 1));

        //CleanUp
        carGroupDbService.deleteCarGroup(carGroupId1);
    }

    @Test
    @Transactional
    void testGetAllCarsInGroup() throws CarGroupNotFoundException {

        //Given
        CarGroup carGroup1 = new CarGroup("Economy");
        CarGroup carGroup2 = new CarGroup("Regular");
        CarGroup carGroup3 = new CarGroup("Premium");

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");
        Car car2 = new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2");
        Car car3 = new Car("ABC 91011", FuelType.DIESEL, Transmission.MANUAL, "Test model 3");

        carGroup1.getCars().add(car1);
        carGroup1.getCars().add(car2);
        carGroup2.getCars().add(car3);

        //When
        carGroupDbService.saveCarGroup(carGroup1);
        carGroupDbService.saveCarGroup(carGroup2);
        carGroupDbService.saveCarGroup(carGroup3);

        Integer carGroupId1 = carGroup1.getCarGroupId();
        Integer carGroupId2 = carGroup2.getCarGroupId();
        Integer carGroupId3 = carGroup3.getCarGroupId();

        List<Car> carsInGroup1 = carGroupDbService.getAllCarsInGroup(carGroupId1);
        List<Car> carsInGroup2 = carGroupDbService.getAllCarsInGroup(carGroupId2);
        List<Car> carsInGroup3 = carGroupDbService.getAllCarsInGroup(carGroupId3);

        //Then
        assertEquals(2, carsInGroup1.size());
        assertEquals(1, carsInGroup2.size());
        assertEquals(0, carsInGroup3.size());

        //CleanUp
        carGroupDbService.deleteCarGroup(carGroupId1);
        carGroupDbService.deleteCarGroup(carGroupId2);
        carGroupDbService.deleteCarGroup(carGroupId3);
    }

    @Test
    @Transactional
    void testGetAllAvailableGroup() throws CarGroupNotFoundException {
        //Given
        CarGroup carGroup1 = new CarGroup("Economy");
        CarGroup carGroup2 = new CarGroup("Regular");

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");
        Car car2 = new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2");
        Car car3 = new Car("ABC 91011", FuelType.DIESEL, Transmission.MANUAL, "Test model 3");

        carGroup1.getCars().add(car1);
        carGroup1.getCars().add(car2);
        carGroup2.getCars().add(car3);

        LocalDate rental1DateFrom = LocalDate.of(2023, 2, 5);
        LocalDate rental1DateTo = LocalDate.of(2023, 2, 12);
        LocalDate rental2DateFrom = LocalDate.of(2023, 2, 10);
        LocalDate rental2DateTo = LocalDate.of(2023, 2, 20);
        LocalDate rental3DateFrom = LocalDate.of(2023, 3, 1);
        LocalDate rental3DateTo = LocalDate.of(2023, 3, 10);

        Order order1 = new Order();
        order1.setCar(car1);
        order1.setDateFrom(rental1DateFrom);
        order1.setDateTo(rental1DateTo);
        Order order2 = new Order();
        order2.setCar(car2);
        order2.setDateFrom(rental2DateFrom);
        order2.setDateTo(rental2DateTo);
        Order order3 = new Order();
        order3.setCar(car3);
        order3.setDateFrom(rental3DateFrom);
        order3.setDateTo(rental3DateTo);

        Rental rental1 = new Rental();
        rental1.setOrder(order1);
        Rental rental2 = new Rental();
        rental2.setOrder(order2);
        Rental rental3 = new Rental();
        rental3.setOrder(order3);

        order1.setRental(rental1);
        order2.setRental(rental2);
        order3.setRental(rental3);

        //When
        carGroupDbService.saveCarGroup(carGroup1);
        carGroupDbService.saveCarGroup(carGroup2);

        Integer carGroupId1 = carGroup1.getCarGroupId();
        Integer carGroupId2 = carGroup2.getCarGroupId();

        rentalRepository.save(rental1);
        rentalRepository.save(rental2);
        rentalRepository.save(rental3);

        Integer rentalId1 = rental1.getRentalId();
        Integer rentalId2 = rental2.getRentalId();
        Integer rentalId3 = rental3.getRentalId();

        List<Car> availableCars1 = carGroupDbService.getAllAvailableGroup(carGroupId1, rental1DateFrom.minusDays(10), rental1DateFrom.minusDays(2));
        List<Car> availableCars2 = carGroupDbService.getAllAvailableGroup(carGroupId2, rental1DateFrom.minusDays(10), rental1DateFrom.minusDays(2));
        List<Car> availableCars3 = carGroupDbService.getAllAvailableGroup(carGroupId1, rental1DateFrom.minusDays(10), rental1DateFrom.plusDays(1));
        List<Car> availableCars4 = carGroupDbService.getAllAvailableGroup(carGroupId2, rental1DateFrom.minusDays(10), rental1DateFrom.plusDays(1));
        List<Car> availableCars5 = carGroupDbService.getAllAvailableGroup(carGroupId1, rental2DateFrom, rental2DateTo);
        List<Car> availableCars6 = carGroupDbService.getAllAvailableGroup(carGroupId2, rental2DateFrom, rental2DateTo);
        List<Car> availableCars7 = carGroupDbService.getAllAvailableGroup(carGroupId1, rental3DateFrom.minusDays(1), rental3DateFrom.plusDays(1));
        List<Car> availableCars8 = carGroupDbService.getAllAvailableGroup(carGroupId2, rental3DateFrom.minusDays(1), rental3DateFrom.plusDays(1));

        //Then
        assertEquals(2, availableCars1.size());
        assertTrue(availableCars1.contains(car1));
        assertTrue(availableCars1.contains(car2));
        assertEquals(1, availableCars2.size());
        assertTrue(availableCars2.contains(car3));
        assertEquals(1, availableCars3.size());
        assertTrue(availableCars3.contains(car2));
        assertEquals(1, availableCars4.size());
        assertTrue(availableCars4.contains(car3));
        assertEquals(0, availableCars5.size());
        assertEquals(1, availableCars6.size());
        assertTrue(availableCars6.contains(car3));
        assertEquals(2, availableCars7.size());
        assertTrue(availableCars7.contains(car1));
        assertTrue(availableCars7.contains(car2));
        assertEquals(0, availableCars8.size());

        //CleanUp
        rentalRepository.deleteById(rentalId1);
        rentalRepository.deleteById(rentalId2);
        rentalRepository.deleteById(rentalId3);
        carGroupDbService.deleteCarGroup(carGroupId1);
        carGroupDbService.deleteCarGroup(carGroupId2);
    }

    @Test
    void testDeleteCarGroup() {
        //Given
        CarGroup carGroup1 = new CarGroup("Economy");
        CarGroup carGroup2 = new CarGroup("Regular");
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        //When
        carGroup1.getCars().add(car1);
        car1.setCarGroup(carGroup1);

        carGroupDbService.saveCarGroup(carGroup1);
        carGroupDbService.saveCarGroup(carGroup2);

        Integer carGroupId1 = carGroup1.getCarGroupId();
        Integer carGroupId2 = carGroup2.getCarGroupId();
        Integer carId1 = car1.getCarId();

        //Then
        assertDoesNotThrow(() -> carGroupDbService.deleteCarGroup(carGroupId1));
        assertDoesNotThrow(() -> carGroupDbService.deleteCarGroup(carGroupId2));
        assertThrows(CarGroupNotFoundException.class, () -> carGroupDbService.deleteCarGroup(carGroupId1));
        assertNotNull(carId1);
        assertThrows(CarNotFoundException.class, ()-> carDbService.getCar(carId1));
    }
}