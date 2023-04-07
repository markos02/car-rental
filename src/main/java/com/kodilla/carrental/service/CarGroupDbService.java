package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.CarGroup;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.repository.CarGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarGroupDbService {

    private final CarGroupRepository carGroupRepository;
    private final CarDbService carDbService;

    public List<CarGroup> getAllCarGroups() {
        return carGroupRepository.findAll();
    }

    public CarGroup getCarGroup(Integer carGroupId) throws CarGroupNotFoundException {
        return carGroupRepository.findById(carGroupId).orElseThrow(CarGroupNotFoundException::new);
    }

    public CarGroup saveCarGroup(CarGroup carGroup) {
        return carGroupRepository.save(carGroup);
    }

    public List<Car> getAllCarsInGroup(Integer carGroupId) {
        return carGroupRepository.findById(carGroupId).get().getCars();
    }

    public List<Car> getAllAvailableGroup(Integer carGroupId, LocalDate dateFrom, LocalDate dateTo) {
        return getAllCarsInGroup(carGroupId).stream()
                .filter(c -> carDbService.checkIfAvailable(c.getCarId(), dateFrom, dateTo))
                .collect(Collectors.toList());
    }

    public void deleteCarGroup(Integer carGroupId) throws CarGroupNotFoundException {
        if (carGroupRepository.existsById(carGroupId)) {
            carGroupRepository.deleteById(carGroupId);
        } else {
            throw new CarGroupNotFoundException();
        }
    }
}
