package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.CarGroup;
import com.kodilla.carrental.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Override
    List<Client> findAll();
}
