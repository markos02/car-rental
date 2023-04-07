package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.exception.DamageNotFoundException;
import com.kodilla.carrental.repository.DamageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DamageDbService {

    private final DamageRepository damageRepository;

    public List<Damage> getCarDamages(Integer carId) {
        return damageRepository.findDamagesByCar_CarId(carId);
    }

    public Damage saveDamage(Damage damage) {
        return damageRepository.save(damage);
    }

    public void deleteDamage(Integer damageId) throws DamageNotFoundException {
        if (damageRepository.existsById(damageId)) {
            damageRepository.deleteById(damageId);
        } else {
            throw new DamageNotFoundException();
        }
    }
}
