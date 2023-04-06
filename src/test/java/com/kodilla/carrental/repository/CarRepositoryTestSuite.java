package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class CarRepositoryTestSuite {

    @Autowired
    CarRepository carRepository;

    @Autowired
    DamageRepository damageRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void testSaveEmptyCar() {
        //Given
        Car car = new Car();

        //When
        carRepository.save(car);
        int id = car.getCarId();
        Optional<Car> readCar = carRepository.findById(id);

        //Then
        assertTrue(readCar.isPresent());

        //CleanUp
        carRepository.deleteById(id);
    }

    @Test
    void testSaveCarWithProperties() {

        String model = "Opel Astra";
        String licensePlate = "ABC 12345";

        //Given
        Car car = new Car();
        car.setModel(model);
        car.setFuelType(FuelType.GASOLINE);
        car.setLicensePlate(licensePlate);
        car.setTransmission(Transmission.AUTOMATIC);

        //When
        carRepository.save(car);
        int id = car.getCarId();
        Optional<Car> readCar = carRepository.findById(id);

        //Then
        assertTrue(readCar.isPresent());
        assertEquals(model, readCar.get().getModel());
        assertEquals(FuelType.GASOLINE, readCar.get().getFuelType());
        assertEquals(licensePlate, readCar.get().getLicensePlate());
        assertEquals(Transmission.AUTOMATIC, readCar.get().getTransmission());

        //CleanUp
        carRepository.deleteById(id);
    }

    @Transactional
    @Test
    void testSaveCarWithDamages() {

        //Given
        Car car = new Car();
        Damage damage1 = new Damage();
        damage1.setCar(car);
        Damage damage2 = new Damage();
        damage2.setCar(car);
        Damage damage3 = new Damage();
        damage3.setCar(car);

        car.getDamages().add(damage1);
        car.getDamages().add(damage2);
        car.getDamages().add(damage3);

        //When
        carRepository.save(car);
        int id = car.getCarId();
        Optional<Car> readCar = carRepository.findById(id);
        List<Damage> readDamages = readCar.get().getDamages();

        //Then
        assertTrue(readCar.isPresent());
        assertEquals(3, readDamages.size());

        //CleanUp
        carRepository.deleteById(id);
    }

    @Transactional
    @Test
    void testSaveCarWithOrders() {

        //Given
        Car car = new Car();
        Order order1 = new Order();
        order1.setCar(car);
        Order order2 = new Order();
        order2.setCar(car);
        Order order3 = new Order();
        order3.setCar(car);

        car.getOrders().add(order1);
        car.getOrders().add(order2);
        car.getOrders().add(order3);

        //When
        carRepository.save(car);
        int id = car.getCarId();
        Optional<Car> readCar = carRepository.findById(id);
        List<Order> readOrders = readCar.get().getOrders();

        //Then
        assertTrue(readCar.isPresent());
        assertEquals(3, readOrders.size());

        //CleanUp
        carRepository.deleteById(id);
    }

    @Transactional
    @Test
    void testDeleteCarWithDamages() {

        //Given
        Car car = new Car();
        Damage damage1 = new Damage();
        damage1.setCar(car);
        Damage damage2 = new Damage();
        damage2.setCar(car);
        Damage damage3 = new Damage();
        damage3.setCar(car);

        car.getDamages().add(damage1);
        car.getDamages().add(damage2);
        car.getDamages().add(damage3);

        //When
        carRepository.save(car);
        int id = car.getCarId();
        int damage1Id = damage1.getDamageId();
        int damage2Id = damage2.getDamageId();
        int damage3Id = damage3.getDamageId();

        Optional<Car> readCar = carRepository.findById(id);
        List<Damage> readDamages = readCar.get().getDamages();
        carRepository.deleteById(id);

        //Then
        assertEquals(damage1Id, readDamages.get(0).getDamageId());
        assertEquals(damage2Id, readDamages.get(1).getDamageId());
        assertEquals(damage3Id, readDamages.get(2).getDamageId());
        assertFalse(damageRepository.existsById(damage1Id));
        assertFalse(damageRepository.existsById(damage2Id));
        assertFalse(damageRepository.existsById(damage3Id));
    }

    @Transactional
    @Test
    void testDeleteCarWithOrders() {

        //Given
        Car car = new Car();
        Order order1 = new Order();
        order1.setCar(car);
        Order order2 = new Order();
        order2.setCar(car);
        Order order3 = new Order();
        order3.setCar(car);

        car.getOrders().add(order1);
        car.getOrders().add(order2);
        car.getOrders().add(order3);

        //When
        carRepository.save(car);
        int id = car.getCarId();
        int order1Id = order1.getOrderId();
        int order2Id = order2.getOrderId();
        int order3Id = order3.getOrderId();

        Optional<Car> readCar = carRepository.findById(id);
        List<Order> readOrders = readCar.get().getOrders();
        carRepository.deleteById(id);

        //Then
        assertEquals(order1Id, readOrders.get(0).getOrderId());
        assertEquals(order2Id, readOrders.get(1).getOrderId());
        assertEquals(order3Id, readOrders.get(2).getOrderId());
        assertFalse(orderRepository.existsById(order1Id));
        assertFalse(orderRepository.existsById(order2Id));
        assertFalse(orderRepository.existsById(order3Id));
    }
}
