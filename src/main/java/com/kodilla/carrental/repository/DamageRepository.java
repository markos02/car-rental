package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Damage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DamageRepository extends CrudRepository<Damage, Integer> {

    @Override
    List<Damage> findAll();

    List<Damage> findDamagesByCar_CarId(Integer carId);
}
