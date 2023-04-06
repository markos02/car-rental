package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Override
    List<Order> findAll();
}
