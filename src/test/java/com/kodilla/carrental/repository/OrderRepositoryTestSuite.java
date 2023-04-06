package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.Client;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderRepositoryTestSuite {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    void testSaveEmptyOrder() {
        //Given
        Order order = new Order();

        //When
        orderRepository.save(order);
        int id = order.getOrderId();
        Optional<Order> readOrder = orderRepository.findById(id);

        //Then
        assertTrue(readOrder.isPresent());

        //CleanUp
        orderRepository.deleteById(id);
    }

    @Test
    void testSaveOrderWithProperties() {
        //Given
        Order order = new Order();
        order.setDateFrom(LocalDate.of(2023, 1, 1));
        order.setDateTo(LocalDate.of(2023, 2, 1));
        order.setStatus(OrderStatus.IN_PROCESS);
        order.setChildSeat(true);
        order.setGps(true);
        order.setExtraDriver(false);
        order.setFuelLevel(0.25);

        //When
        orderRepository.save(order);
        int id = order.getOrderId();
        Optional<Order> readOrder = orderRepository.findById(id);

        //Then
        assertTrue(readOrder.isPresent());
        assertEquals(LocalDate.of(2023, 1, 1), readOrder.get().getDateFrom());
        assertEquals(LocalDate.of(2023, 2, 1), readOrder.get().getDateTo());
        assertEquals(OrderStatus.IN_PROCESS, readOrder.get().getStatus());
        assertTrue(readOrder.get().isChildSeat());
        assertTrue(readOrder.get().isGps());
        assertFalse(readOrder.get().isExtraDriver());
        assertEquals(0.25, readOrder.get().getFuelLevel());

        //CleanUp
        orderRepository.deleteById(id);
    }

    @Test
    void testSaveOrderWithClient() {
        //Given
        Client client = new Client();
        clientRepository.save(client);
        int clientId = client.getClientId();

        Order order = new Order();
        order.setClient(client);

        //When
        orderRepository.save(order);
        int id = order.getOrderId();
        Optional<Order> readOrder = orderRepository.findById(id);

        //Then
        assertTrue(readOrder.isPresent());
        assertNotNull(readOrder.get().getClient());
        assertTrue(clientRepository.existsById(clientId));

        //CleanUp
        orderRepository.deleteById(id);
        clientRepository.deleteById(clientId);
    }

    @Test
    void testSaveOrderWithRental() {
        //Given
        Rental rental = new Rental();
        Order order = new Order();
        rental.setOrder(order);

        rentalRepository.save(rental);
        int rentalId = rental.getRentalId();;

        order.setRental(rental);

        //When
        orderRepository.save(order);
        int id = order.getOrderId();
        Optional<Order> readOrder = orderRepository.findById(id);

        //Then
        assertTrue(readOrder.isPresent());
        assertNotNull(readOrder.get().getRental());
        assertTrue(rentalRepository.existsById(rentalId));

        //CleanUp
        orderRepository.deleteById(id);
        rentalRepository.deleteById(rentalId);
    }
}
