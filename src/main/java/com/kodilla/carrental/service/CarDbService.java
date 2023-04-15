package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.repository.CarGroupRepository;
import com.kodilla.carrental.repository.CarRepository;
import com.kodilla.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarDbService {

    private final CarRepository carRepository;
    private final CarGroupRepository carGroupRepository;
    private final RentalRepository rentalRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCar(Integer carId) throws CarNotFoundException {
        return carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
    }

    public Car createNewCar(Car car, Integer carGroupId) throws CarGroupNotFoundException {
        if (carGroupId != 0) {
            car.setCarGroup(carGroupRepository.findById(carGroupId).orElseThrow(CarGroupNotFoundException::new));
        }
        return carRepository.save(car);
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public boolean checkIfAvailable(Integer carId, LocalDate dateFrom, LocalDate dateTo) {
        List<Rental> retrievedRentals = rentalRepository.retrieveRentalsBetweenDates(dateFrom, dateTo).stream()
                .filter(r -> r.getOrder().getCar().getCarId().equals(carId))
                .collect(Collectors.toList());

        return retrievedRentals.size() == 0;
    }

    public List<Car> getAllAvailable(LocalDate dateFrom, LocalDate dateTo) {
        Set<Car> rentedCars = rentalRepository.retrieveRentalsBetweenDates(dateFrom, dateTo).stream()
                .map(rental -> rental.getOrder().getCar())
                .collect(Collectors.toSet());

        List<Car> availableCars = getAllCars();
        availableCars.removeAll(rentedCars);

        return availableCars;
    }

    public void deleteCar(Integer carId) throws CarNotFoundException {
        if (carRepository.existsById(carId)) {
            carRepository.deleteById(carId);
        } else {
            throw new CarNotFoundException();
        }
    }
}
