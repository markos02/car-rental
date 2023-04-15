package com.kodilla.carrental.mapper;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.exception.OrderNotFoundException;
import com.kodilla.carrental.repository.DamageRepository;
import com.kodilla.carrental.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalMapper {

    private final OrderRepository orderRepository;
    private final DamageRepository damageRepository;

    public Rental mapToRental(RentalDto rentalDto) throws OrderNotFoundException {

        Order order = orderRepository.findById(rentalDto.getOrderId()).orElseThrow(OrderNotFoundException::new);
        List<Damage> damages = rentalDto.getDamagesIds().stream()
                .map(i -> damageRepository.findById(i).get())
                .collect(Collectors.toList());

        return new Rental(
                rentalDto.getRentalId(),
                order,
                damages,
                rentalDto.getStatus()
        );
    }

    public RentalDto mapToRentalDto(Rental rental) {

        List<Integer> damagesIds = rental.getDamages().stream()
                .map(Damage::getDamageId)
                .collect(Collectors.toList());

        return new RentalDto(
                rental.getRentalId(),
                rental.getOrder().getOrderId(),
                damagesIds,
                rental.getStatus()
        );
    }

    public List<RentalDto> mapToRentalDtoList(List<Rental> rentalList) {
        return rentalList.stream()
                .map(this::mapToRentalDto)
                .collect(Collectors.toList());
    }
}
