package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.CarGroup;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarGroupRepositoryTestSuite {

    @Autowired
    private CarGroupRepository carGroupRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void testSaveEmptyCarGroup() {
        //Given
        CarGroup carGroup = new CarGroup();

        //When
        carGroupRepository.save(carGroup);
        int id = carGroup.getCarGroupId();
        Optional<CarGroup> readCarGroup = carGroupRepository.findById(id);

        //Then
        assertTrue(readCarGroup.isPresent());

        //CleanUp
        carGroupRepository.deleteById(id);
    }

    @Test
    void testSaveCarGroupWithName() {

        String groupName = "Economy";

        //Given
        CarGroup carGroup = new CarGroup(groupName);

        //When
        carGroupRepository.save(carGroup);
        int id = carGroup.getCarGroupId();
        Optional<CarGroup> readCarGroup = carGroupRepository.findById(id);

        //Then
        assertTrue(readCarGroup.isPresent());
        assertEquals(groupName, readCarGroup.get().getName());

        //CleanUp
        carGroupRepository.deleteById(id);
    }

    @Transactional
    @Test
    void testSaveCarGroupWithCars() {

        String groupName = "Economy";

        //Given
        CarGroup carGroup = new CarGroup(groupName);
        Car car1 = new Car();
        car1.setCarGroup(carGroup);
        Car car2 = new Car();
        car2.setCarGroup(carGroup);
        Car car3 = new Car();
        car3.setCarGroup(carGroup);

        carGroup.getCars().add(car1);
        carGroup.getCars().add(car2);
        carGroup.getCars().add(car3);

        //When
        carGroupRepository.save(carGroup);
        int id = carGroup.getCarGroupId();

        Optional<CarGroup> readCarGroup = carGroupRepository.findById(id);
        List<Car> readCars = readCarGroup.get().getCars();

        //Then
        assertTrue(readCarGroup.isPresent());
        assertEquals(3, readCars.size());

        //CleanUp
        carGroupRepository.deleteById(id);
    }

    @Transactional
    @Test
    void testDeleteCarGroupWithCars() {

        String groupName = "Economy";

        //Given
        CarGroup carGroup = new CarGroup(groupName);
        Car car1 = new Car();
        car1.setCarGroup(carGroup);
        Car car2 = new Car();
        car2.setCarGroup(carGroup);
        Car car3 = new Car();
        car3.setCarGroup(carGroup);

        carGroup.getCars().add(car1);
        carGroup.getCars().add(car2);
        carGroup.getCars().add(car3);

        //When
        carGroupRepository.save(carGroup);
        int id = carGroup.getCarGroupId();
        int car1Id = car1.getCarId();
        int car2Id = car2.getCarId();
        int car3Id = car3.getCarId();

        Optional<CarGroup> readCarGroup = carGroupRepository.findById(id);
        List<Car> readCars = readCarGroup.get().getCars();
        carGroupRepository.deleteById(id);

        //Then
        assertEquals(car1Id,readCars.get(0).getCarId());
        assertEquals(car2Id,readCars.get(1).getCarId());
        assertEquals(car3Id,readCars.get(2).getCarId());
        assertFalse(carRepository.existsById(car1Id));
        assertFalse(carRepository.existsById(car2Id));
        assertFalse(carRepository.existsById(car3Id));
    }
}
