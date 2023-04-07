package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.CarGroup;
import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.repository.CarGroupRepository;
import com.kodilla.carrental.repository.RentalRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarDbServiceTest {

    @Autowired
    CarDbService carDbService;

    @Autowired
    CarGroupRepository carGroupRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Test
    void testGetAllCars() throws CarNotFoundException {
        //Given
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");
        Car car2 = new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2");
        Car car3 = new Car("ABC 91011", FuelType.DIESEL, Transmission.MANUAL, "Test model 3");

        //When
        carDbService.saveCar(car1);
        carDbService.saveCar(car2);
        carDbService.saveCar(car3);

        Integer carId1 = car1.getCarId();
        Integer carId2 = car2.getCarId();
        Integer carId3 = car3.getCarId();

        //Then
        assertEquals(3, carDbService.getAllCars().size());

        //CleanUp
        carDbService.deleteCar(carId1);
        carDbService.deleteCar(carId2);
        carDbService.deleteCar(carId3);
    }

    @Test
    void testGetAllCarsNoCars() {
        //Given

        //When
        List<Car> retrievedCars = carDbService.getAllCars();

        //Then
        assertNotNull(retrievedCars);
        assertEquals(0, retrievedCars.size());

        //CleanUp
    }

    @Test
    void testGetCar() throws CarNotFoundException {
        //Given
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        //When
        carDbService.saveCar(car1);
        Integer carId1 = car1.getCarId();
        Car retrievedCar = carDbService.getCar(carId1);

        //Then
        assertNotNull(retrievedCar);
        assertEquals("ABC 1234", retrievedCar.getLicensePlate());
        assertEquals(FuelType.GASOLINE, retrievedCar.getFuelType());
        assertEquals(Transmission.AUTOMATIC, retrievedCar.getTransmission());
        assertEquals("Test model 1", retrievedCar.getModel());

        //CleanUp
        carDbService.deleteCar(carId1);
    }

    @Test
    void testGetCarNotFound() throws CarNotFoundException {
        //Given
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        //When
        carDbService.saveCar(car1);
        Integer carId1 = car1.getCarId();

        //Then
        assertThrows(CarNotFoundException.class, () -> carDbService.getCar(carId1 + 1));

        //CleanUp
        carDbService.deleteCar(carId1);
    }

    @Test
    void testCreateNewCar() throws CarGroupNotFoundException, CarNotFoundException {
        //Given
        CarGroup carGroup = new CarGroup("Test group");
        carGroupRepository.save(carGroup);
        Integer carGroupId = carGroup.getCarGroupId();

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        //When
        carDbService.createNewCar(car1, carGroupId);
        Integer carId1 = car1.getCarId();
        Car retrievedCar = carDbService.getCar(carId1);

        //Then
        assertNotNull(retrievedCar);
        assertEquals(carGroupId, retrievedCar.getCarGroup().getCarGroupId());
        assertEquals(carGroup.getName(), retrievedCar.getCarGroup().getName());
        assertEquals("ABC 1234", retrievedCar.getLicensePlate());
        assertEquals(FuelType.GASOLINE, retrievedCar.getFuelType());
        assertEquals(Transmission.AUTOMATIC, retrievedCar.getTransmission());
        assertEquals("Test model 1", retrievedCar.getModel());

        //CleanUp
        carDbService.deleteCar(carId1);
        carGroupRepository.deleteById(carGroupId);
    }

    @Test
    void testCreateNewCarNoGroup() throws CarGroupNotFoundException, CarNotFoundException {
        //Given
        CarGroup carGroup = new CarGroup("Test group");
        carGroupRepository.save(carGroup);
        Integer carGroupId = carGroup.getCarGroupId();

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        //When
        carDbService.createNewCar(car1, 0);
        Integer carId1 = car1.getCarId();
        Car retrievedCar = carDbService.getCar(carId1);

        //Then
        assertNotNull(retrievedCar);
        assertNull(retrievedCar.getCarGroup());
        assertEquals("ABC 1234", retrievedCar.getLicensePlate());
        assertEquals(FuelType.GASOLINE, retrievedCar.getFuelType());
        assertEquals(Transmission.AUTOMATIC, retrievedCar.getTransmission());
        assertEquals("Test model 1", retrievedCar.getModel());

        //CleanUp
        carDbService.deleteCar(carId1);
        carGroupRepository.deleteById(carGroupId);
    }

    @Transactional
    @Test
    void testCheckIfAvailable() throws CarNotFoundException {
        //Given
        LocalDate rental1DateFrom = LocalDate.of(2023, 2, 5);
        LocalDate rental1DateTo = LocalDate.of(2023, 2, 12);
        LocalDate rental2DateFrom = LocalDate.of(2023, 2, 10);
        LocalDate rental2DateTo = LocalDate.of(2023, 2, 20);
        LocalDate rental3DateFrom = LocalDate.of(2023, 3, 1);
        LocalDate rental3DateTo = LocalDate.of(2023, 3, 10);

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");
        Car car2 = new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2");

        Order order1 = new Order();
        order1.setCar(car1);
        order1.setDateFrom(rental1DateFrom);
        order1.setDateTo(rental1DateTo);
        Order order2 = new Order();
        order2.setCar(car2);
        order2.setDateFrom(rental2DateFrom);
        order2.setDateTo(rental2DateTo);
        Order order3 = new Order();
        order3.setCar(car1);
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
        carDbService.saveCar(car1);
        int carId1 = car1.getCarId();
        carDbService.saveCar(car2);
        int carId2 = car2.getCarId();
        rentalRepository.save(rental1);
        int rentalId1 = rental1.getRentalId();
        rentalRepository.save(rental2);
        int rentalId2 = rental2.getRentalId();
        rentalRepository.save(rental3);
        int rentalId3 = rental3.getRentalId();

        //Then
        assertTrue(carDbService.checkIfAvailable(carId1, rental1DateFrom.minusDays(10), rental1DateFrom.minusDays(1)));
        assertFalse(carDbService.checkIfAvailable(carId1, rental1DateFrom.minusDays(1), rental1DateFrom));
        assertFalse(carDbService.checkIfAvailable(carId1, rental1DateFrom.plusDays(1), rental1DateTo.minusDays(1)));
        assertFalse(carDbService.checkIfAvailable(carId1, rental1DateTo, rental1DateTo.plusDays(1)));
        assertTrue(carDbService.checkIfAvailable(carId1, rental1DateTo.plusDays(1), rental3DateFrom.minusDays(1)));
        assertFalse(carDbService.checkIfAvailable(carId1, rental1DateTo.plusDays(1), rental3DateFrom));
        assertFalse(carDbService.checkIfAvailable(carId2, rental2DateFrom, rental2DateTo));
        assertFalse(carDbService.checkIfAvailable(carId2, rental2DateFrom.minusDays(1), rental2DateTo.plusDays(1)));
        assertTrue(carDbService.checkIfAvailable(carId2, rental2DateTo.plusDays(1), rental2DateTo.plusDays(30)));

        //CleanUp

        try {
            carDbService.deleteCar(carId1);
            carDbService.deleteCar(carId2);
            rentalRepository.deleteById(rentalId1);
            rentalRepository.deleteById(rentalId2);
            rentalRepository.deleteById(rentalId3);
        } catch (Exception e) {
            //do nothing
        }
    }

    @Transactional
    @Test
    void testGetAllAvailable() throws CarNotFoundException {
        //Given
        LocalDate rental1DateFrom = LocalDate.of(2023, 2, 5);
        LocalDate rental1DateTo = LocalDate.of(2023, 2, 12);
        LocalDate rental2DateFrom = LocalDate.of(2023, 2, 10);
        LocalDate rental2DateTo = LocalDate.of(2023, 2, 20);
        LocalDate rental3DateFrom = LocalDate.of(2023, 3, 1);
        LocalDate rental3DateTo = LocalDate.of(2023, 3, 10);

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");
        Car car2 = new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2");

        Order order1 = new Order();
        order1.setCar(car1);
        order1.setDateFrom(rental1DateFrom);
        order1.setDateTo(rental1DateTo);
        Order order2 = new Order();
        order2.setCar(car2);
        order2.setDateFrom(rental2DateFrom);
        order2.setDateTo(rental2DateTo);
        Order order3 = new Order();
        order3.setCar(car1);
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
        carDbService.saveCar(car1);
        int carId1 = car1.getCarId();
        carDbService.saveCar(car2);
        int carId2 = car2.getCarId();
        rentalRepository.save(rental1);
        int rentalId1 = rental1.getRentalId();
        rentalRepository.save(rental2);
        int rentalId2 = rental2.getRentalId();
        rentalRepository.save(rental3);
        int rentalId3 = rental3.getRentalId();

        List<Car> availableCars1 = carDbService.getAllAvailable(rental1DateFrom.minusDays(10), rental1DateFrom.minusDays(2));
        List<Car> availableCars2 = carDbService.getAllAvailable(rental1DateFrom.minusDays(10), rental1DateFrom.plusDays(1));
        List<Car> availableCars3 = carDbService.getAllAvailable(rental2DateFrom, rental2DateTo);
        List<Car> availableCars4 = carDbService.getAllAvailable(rental2DateTo.minusDays(1), rental2DateTo.plusDays(1));
        List<Car> availableCars5 = carDbService.getAllAvailable(rental3DateTo.plusDays(10), rental3DateTo.plusDays(12));

        //Then
        assertEquals(2, availableCars1.size());
        assertTrue(availableCars1.contains(car1));
        assertTrue(availableCars1.contains(car2));
        assertEquals(1, availableCars2.size());
        assertTrue(availableCars2.contains(car2));
        assertEquals(0, availableCars3.size());
        assertEquals(1, availableCars4.size());
        assertTrue(availableCars4.contains(car1));
        assertEquals(2, availableCars5.size());

        //CleanUp

        try {
            carDbService.deleteCar(carId1);
            carDbService.deleteCar(carId2);
            rentalRepository.deleteById(rentalId1);
            rentalRepository.deleteById(rentalId2);
            rentalRepository.deleteById(rentalId3);
        } catch (Exception e) {
            // do nothing
        }
    }

    @Test
    void testDeleteCar() throws CarNotFoundException {
        //Given
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        //When
        carDbService.saveCar(car1);
        Integer carId1 = car1.getCarId();

        //Then
        assertDoesNotThrow(() -> carDbService.deleteCar(carId1));
        assertThrows(CarNotFoundException.class, () -> carDbService.deleteCar(carId1 - 1));

        //CleanUp
    }
}