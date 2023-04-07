package com.kodilla.carrental.mapper;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.DamageDto;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.exception.RentalNotFoundException;
import com.kodilla.carrental.repository.CarRepository;
import com.kodilla.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DamageMapper {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    public Damage mapToDamage(DamageDto damageDto) throws CarNotFoundException, RentalNotFoundException {

        Car car = carRepository.findById(damageDto.getCarId()).orElseThrow(CarNotFoundException::new);
        Rental rental = rentalRepository.findById(damageDto.getRentalId()).orElseThrow(RentalNotFoundException::new);

        return new Damage(
                damageDto.getDamageId(),
                damageDto.getDate(),
                car,
                rental,
                damageDto.getDescription()
        );
    }

    public DamageDto mapToDamageDto(Damage damage) {
        return new DamageDto(
                damage.getDamageId(),
                damage.getDate(),
                damage.getCar().getCarId(),
                damage.getRental().getRentalId(),
                damage.getDescription()
        );
    }

    public List<DamageDto> mapToDamageDtoList(List<Damage> damageList) {
        return damageList.stream()
                .map(this::mapToDamageDto)
                .collect(Collectors.toList());
    }
}
