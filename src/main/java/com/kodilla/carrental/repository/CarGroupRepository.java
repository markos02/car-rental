package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.CarGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarGroupRepository extends CrudRepository<CarGroup, Integer> {

}
