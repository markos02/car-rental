package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Client;
import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.OrderStatus;
import com.kodilla.carrental.domain.enums.RentalStatus;
import com.kodilla.carrental.domain.enums.Transmission;
import com.kodilla.carrental.exception.OrderNotFoundException;
import com.kodilla.carrental.exception.RentalNotFoundException;
import com.kodilla.carrental.repository.CarRepository;
import com.kodilla.carrental.repository.ClientRepository;
import com.kodilla.carrental.repository.RentalRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderDbServiceTest {

    @Autowired
    OrderDbService orderDbService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private RentalRepository rentalRepository;

    @Test
    void testGetAllOrders() throws OrderNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.of(2023, 1, 1);
        LocalDate order1DateTo = LocalDate.of(2023, 2, 1);
        LocalDate order2DateFrom = LocalDate.of(2023, 2, 1);
        LocalDate order2DateTo = LocalDate.of(2023, 3, 1);
        LocalDate order3DateFrom = LocalDate.of(2023, 4, 1);
        LocalDate order3DateTo = LocalDate.of(2023, 5, 1);

        Order order1 = new Order();
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.IN_PROCESS);

        Order order2 = new Order();
        order2.setDateFrom(order2DateFrom);
        order2.setDateTo(order2DateTo);
        order2.setStatus(OrderStatus.COMPLETED);

        Order order3 = new Order();
        order3.setDateFrom(order3DateFrom);
        order3.setDateTo(order3DateTo);
        order3.setStatus(OrderStatus.IN_PROCESS);

        //When
        orderDbService.saveOrder(order1);
        orderDbService.saveOrder(order2);
        orderDbService.saveOrder(order3);

        Integer orderId1 = order1.getOrderId();
        Integer orderId2 = order2.getOrderId();
        Integer orderId3 = order3.getOrderId();

        List<Order> savedOrders = orderDbService.getAllOrders();

        //Then
        assertEquals(3, savedOrders.size());

        //CleanUp
        orderDbService.deleteOrder(orderId1);
        orderDbService.deleteOrder(orderId2);
        orderDbService.deleteOrder(orderId3);
    }

    @Test
    void testGetOrder() throws OrderNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.of(2023, 1, 1);
        LocalDate order1DateTo = LocalDate.of(2023, 2, 1);

        Order order1 = new Order();
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.IN_PROCESS);

        //When
        orderDbService.saveOrder(order1);
        Integer orderId1 = order1.getOrderId();

        Order savedOrder = orderDbService.getOrder(orderId1);

        //Then
        assertNotNull(savedOrder);
        assertEquals(order1DateFrom, savedOrder.getDateFrom());
        assertEquals(order1DateTo, savedOrder.getDateTo());

        //CleanUp
        orderDbService.deleteOrder(orderId1);
    }

    @Test
    void testGetOrderNotFound() throws OrderNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.of(2023, 1, 1);
        LocalDate order1DateTo = LocalDate.of(2023, 2, 1);

        Order order1 = new Order();
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.IN_PROCESS);

        //When
        orderDbService.saveOrder(order1);
        Integer orderId1 = order1.getOrderId();

        Order savedOrder = orderDbService.getOrder(orderId1);
        orderDbService.deleteOrder(orderId1);

        //Then
        assertNotNull(savedOrder);
        assertThrows(OrderNotFoundException.class, () -> orderDbService.getOrder(orderId1));
    }

    @Test
    @Transactional
    void testStartRental() throws OrderNotFoundException, RentalNotFoundException {
        //Given
        LocalDate order1DateFrom = LocalDate.of(2023, 1, 1);
        LocalDate order1DateTo = LocalDate.of(2023, 2, 1);

        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        Order order1 = new Order();
        order1.setClient(client1);
        order1.setCar(car1);
        order1.setDateFrom(order1DateFrom);
        order1.setDateTo(order1DateTo);
        order1.setStatus(OrderStatus.IN_PROCESS);
        order1.setChildSeat(true);
        order1.setGps(true);
        order1.setExtraDriver(false);
        order1.setFuelLevel(0.25);

        //When
        clientRepository.save(client1);
        Integer clientId1 = client1.getClientId();

        carRepository.save(car1);
        Integer carId1 = car1.getCarId();

        orderDbService.saveOrder(order1);
        Integer orderId1 = order1.getOrderId();

        Rental rental = orderDbService.startRental(orderId1);
        Integer rentalId1 = rental.getRentalId();

        Rental retrievedRental = rentalRepository.findById(rentalId1).orElseThrow(RentalNotFoundException::new);
        Order retrievedOrder = orderDbService.getOrder(orderId1);

        //Then
        assertNotNull(retrievedRental );
        assertEquals(retrievedRental.getOrder().getOrderId(),orderId1);
        assertEquals(RentalStatus.ORDERED, retrievedRental.getStatus());
        assertEquals(OrderStatus.COMPLETED,retrievedOrder.getStatus());

        //CleanUp
        rentalRepository.deleteById(rentalId1);
        carRepository.deleteById(carId1);
        clientRepository.deleteById(clientId1);
    }
}