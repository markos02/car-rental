package com.kodilla.carrental.controller;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.exception.OrderNotFoundException;
import com.kodilla.carrental.exception.RentalNotFoundException;
import com.kodilla.carrental.mapper.RentalMapper;
import com.kodilla.carrental.service.RentalDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalMapper rentalMapper;
    private final RentalDbService rentalDbService;

    @GetMapping
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        List<Rental> rentals = rentalDbService.getAllRentals();
        return ResponseEntity.ok(rentalMapper.mapToRentalDtoList(rentals));
    }

    @GetMapping("/active")
    public ResponseEntity<List<RentalDto>> getActiveRentals() {
        List<Rental> activeRentals = rentalDbService.getActiveRentals();
        return ResponseEntity.ok(rentalMapper.mapToRentalDtoList(activeRentals));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<RentalDto>> getOverdueRentals() {
        List<Rental> overdueRentals = rentalDbService.getOverdueRentals();
        return ResponseEntity.ok(rentalMapper.mapToRentalDtoList(overdueRentals));
    }

    @GetMapping(value = "/{rentalId}")
    public ResponseEntity<RentalDto> getRental(@PathVariable Integer rentalId) throws RentalNotFoundException {
        return ResponseEntity.ok(rentalMapper.mapToRentalDto(rentalDbService.getRental(rentalId)));
    }

    @PostMapping
    public ResponseEntity<RentalDto> createRental(@RequestBody RentalDto rentalDto) throws OrderNotFoundException {
        Rental rental = rentalMapper.mapToRental(rentalDto);
        Rental savedRental = rentalDbService.saveRental(rental);
        return ResponseEntity.ok(rentalMapper.mapToRentalDto(savedRental));
    }

    @PutMapping
    public ResponseEntity<RentalDto> updateRental(@RequestBody RentalDto rentalDto) throws OrderNotFoundException {
        Rental rental = rentalMapper.mapToRental(rentalDto);
        Rental savedRental = rentalDbService.saveRental(rental);
        return ResponseEntity.ok(rentalMapper.mapToRentalDto(savedRental));
    }

    @PutMapping("/return")
    public ResponseEntity<RentalDto> returnCar(@RequestBody ReturnCarDto returnCarDto) throws RentalNotFoundException {
        Rental completedRental = rentalDbService.returnCar(returnCarDto.getRentalId(), returnCarDto.getReturnDate(), returnCarDto.getFuelLevel());
        return ResponseEntity.ok(rentalMapper.mapToRentalDto(completedRental));
    }
}
