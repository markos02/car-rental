package com.kodilla.carrental.controller;

import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.DamageDto;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.exception.DamageNotFoundException;
import com.kodilla.carrental.exception.RentalNotFoundException;
import com.kodilla.carrental.mapper.DamageMapper;
import com.kodilla.carrental.service.DamageDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/damages")
public class DamageController {

    private final DamageMapper damageMapper;
    private final DamageDbService damageDbService;

    @GetMapping(value = "/{carId}")
    public ResponseEntity<List<DamageDto>> getCarDamages(@PathVariable Integer carId) {
        return ResponseEntity.ok(damageMapper.mapToDamageDtoList(damageDbService.getCarDamages(carId)));
    }

    @PostMapping
    public ResponseEntity<DamageDto> createDamage(@RequestBody DamageDto damageDto) throws RentalNotFoundException, CarNotFoundException {
        Damage damage = damageMapper.mapToDamage(damageDto);
        Damage savedDamage = damageDbService.saveDamage(damage);
        return ResponseEntity.ok(damageMapper.mapToDamageDto(savedDamage));
    }

    @DeleteMapping(value = "/{damageId}")
    public ResponseEntity<Void> deleteDamage(@PathVariable Integer damageId) throws DamageNotFoundException {
        damageDbService.deleteDamage(damageId);
        return ResponseEntity.ok().build();
    }
}
