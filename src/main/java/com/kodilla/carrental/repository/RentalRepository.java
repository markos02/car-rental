package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Rental;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer> {

@Query(nativeQuery = true)
    List<Rental> retrieveRentalsBetweenDates(@Param("RENTAL_START") LocalDate rentalStart, @Param("RENTAL_END") LocalDate rentalEnd);
}
