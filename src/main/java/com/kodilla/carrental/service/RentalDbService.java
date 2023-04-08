package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.ReturnCarDto;
import com.kodilla.carrental.domain.enums.RentalStatus;
import com.kodilla.carrental.exception.RentalNotFoundException;
import com.kodilla.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class RentalDbService {

    private final RentalRepository rentalRepository;
    private final DamageDbService damageDbService;
    private final ExtraFeesService extraFeesService;

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

    public Rental returnCar(Integer rentalId, LocalDate returnDate, Double fuelLevel) throws RentalNotFoundException {

        Rental rental = getRental(rentalId);

        List<Damage> newDamages = damageDbService.getRentalDamages(rentalId);
        if (newDamages.size() != 0) {
            extraFeesService.newDamagesFee(newDamages);
        }

        Long overdue = DAYS.between(returnDate,rental.getOrder().getDateTo());
        if (overdue < 0) {
            extraFeesService.overdueFee(overdue);
        }

        Double fuelDifference = fuelLevel - rental.getOrder().getFuelLevel();
        if (fuelDifference < 0) {
            extraFeesService.fuelFee(fuelDifference);
        }

        rental.setStatus(RentalStatus.RETURNED);

        return saveRental(rental);
    }
}
