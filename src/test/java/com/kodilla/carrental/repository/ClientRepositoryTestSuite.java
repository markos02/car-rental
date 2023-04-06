package com.kodilla.carrental.repository;

import com.kodilla.carrental.domain.CarGroup;
import com.kodilla.carrental.domain.Client;
import com.kodilla.carrental.domain.Order;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ClientRepositoryTestSuite {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveEmptyClient() {
        //Given
        Client client = new Client();

        //When
        clientRepository.save(client);
        int id = client.getClientId();
        Optional<Client> readClient = clientRepository.findById(id);

        //Then
        assertTrue(readClient.isPresent());

        //CleanUp
        clientRepository.deleteById(id);
    }

    @Test
    void testSaveClientWithProperties() {
        //Given
        Client client = new Client();
        client.setFirstname("John");
        client.setLastname("Smith");

        //When
        clientRepository.save(client);
        int id = client.getClientId();
        Optional<Client> readClient = clientRepository.findById(id);

        //Then
        assertTrue(readClient.isPresent());
        assertEquals("John", readClient.get().getFirstname());
        assertEquals("Smith", readClient.get().getLastname());

        //CleanUp
        clientRepository.deleteById(id);
    }

    @Transactional
    @Test
    void testSaveClientWithOrders() {
        //Given
        Client client = new Client();
        client.setFirstname("John");
        client.setLastname("Smith");

        Order order1 = new Order();
        order1.setClient(client);
        Order order2 = new Order();
        order2.setClient(client);
        Order order3 = new Order();
        order3.setClient(client);

        client.getOrders().add(new Order());
        client.getOrders().add(new Order());
        client.getOrders().add(new Order());

        //When
        clientRepository.save(client);
        int id = client.getClientId();
        Optional<Client> readClient = clientRepository.findById(id);
        List<Order> readOrders = readClient.get().getOrders();

        //Then
        assertTrue(readClient.isPresent());
        assertEquals(3, readOrders.size());

        //CleanUp
        clientRepository.deleteById(id);
    }

    @Transactional
    @Test
    void testDeleteClientWithOrders() {
        //Given
        Client client = new Client();
        client.setFirstname("John");
        client.setLastname("Smith");

        Order order1 = new Order();
        order1.setClient(client);
        Order order2 = new Order();
        order2.setClient(client);
        Order order3 = new Order();
        order3.setClient(client);

        client.getOrders().add(order1);
        client.getOrders().add(order2);
        client.getOrders().add(order3);

        //When
        clientRepository.save(client);
        int id = client.getClientId();
        int order1Id = order1.getOrderId();
        int order2Id = order2.getOrderId();
        int order3Id = order3.getOrderId();

        Optional<Client> readClient = clientRepository.findById(id);
        List<Order> readOrders = readClient.get().getOrders();
        clientRepository.deleteById(id);

        //Then
        assertEquals(order1Id, readOrders.get(0).getOrderId());
        assertEquals(order2Id, readOrders.get(1).getOrderId());
        assertEquals(order3Id, readOrders.get(2).getOrderId());
        assertFalse(orderRepository.existsById(order1Id));
        assertFalse(orderRepository.existsById(order2Id));
        assertFalse(orderRepository.existsById(order3Id));
    }
}
