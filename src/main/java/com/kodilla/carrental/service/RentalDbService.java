package com.kodilla.carrental.service;


import com.kodilla.carrental.client.GasPriceClient;
import com.kodilla.carrental.client.NbpClient;
import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.domain.enums.RentalStatus;
import com.kodilla.carrental.exception.RentalNotFoundException;
import com.kodilla.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class RentalDbService {

    private final RentalRepository rentalRepository;
    private final DamageDbService damageDbService;
    private final ExtraFeeService extraFeeService;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getActiveRentals() {
        return rentalRepository.findByStatus(RentalStatus.RENTED);
    }

    public List<Rental> getOverdueRentals() {
        List<Rental> activeRentals = rentalRepository.findByStatus(RentalStatus.RENTED);

        return activeRentals.stream()
                .filter(r -> r.getOrder().getDateTo().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public Rental getRental(Integer rentalId) throws RentalNotFoundException {
        return rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new);
    }

    public Rental saveRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public void deleteRental(Integer rentalId) throws RentalNotFoundException {
        if (rentalRepository.existsById(rentalId)) {
            rentalRepository.deleteById(rentalId);
        } else {
            throw new RentalNotFoundException();
        }
    }

    public List<Fee> returnCar(Integer rentalId, LocalDate returnDate, Double fuelLevel) throws RentalNotFoundException {

        List<Fee> fees = new ArrayList<>();
        Rental rental = getRental(rentalId);

        List<Damage> newDamages = damageDbService.getRentalDamages(rentalId);
        if (newDamages.size() != 0) {
            fees.add(extraFeeService.newDamagesFee(newDamages));
        }

        Long overdue = DAYS.between(rental.getOrder().getDateTo(), returnDate);
        if (overdue > 0) {
            fees.add(extraFeeService.overdueFee(overdue));
        }

        Double fuelDifference = rental.getOrder().getFuelLevel() - fuelLevel;
        if (fuelDifference > 0) {
            fees.add(extraFeeService.fuelFee(fuelDifference));
        }

        rental.setStatus(RentalStatus.RETURNED);

        return fees;
    }
}
