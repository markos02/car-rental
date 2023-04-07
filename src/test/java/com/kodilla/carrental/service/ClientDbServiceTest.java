package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Client;
import com.kodilla.carrental.exception.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientDbServiceTest {

    @Autowired
    ClientDbService clientDbService;

    @Test
    void testGetAllClients() throws ClientNotFoundException {
        //Given
        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        Client client2 = new Client();
        client2.setFirstname("Jane");
        client2.setLastname("Doe");

        Client client3 = new Client();
        client3.setFirstname("Xxx");
        client3.setLastname("Yyy");

        //When
        clientDbService.saveClient(client1);
        clientDbService.saveClient(client2);
        clientDbService.saveClient(client3);
        Integer clientId1 = client1.getClientId();
        Integer clientId2 = client2.getClientId();
        Integer clientId3 = client3.getClientId();

        List<Client> clients = clientDbService.getAllClients();

        //Then
        assertNotNull(clients);
        assertEquals(3, clients.size());
        assertEquals("John", clients.get(0).getFirstname());

        //CleanUp
        clientDbService.deleteClient(clientId1);
        clientDbService.deleteClient(clientId2);
        clientDbService.deleteClient(clientId3);
    }

    @Test
    void testGetClient() throws ClientNotFoundException {
        //Given
        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        Client client2 = new Client();
        client2.setFirstname("Jane");
        client2.setLastname("Doe");

        //When
        clientDbService.saveClient(client1);
        clientDbService.saveClient(client2);
        Integer clientId1 = client1.getClientId();
        Integer clientId2 = client2.getClientId();

        //Then
        assertEquals("John", clientDbService.getClient(clientId1).getFirstname());
        assertEquals("Doe", clientDbService.getClient(clientId2).getLastname());

        //CleanUp
        clientDbService.deleteClient(clientId1);
        clientDbService.deleteClient(clientId2);
    }

    @Test
    void TestDeleteClient() {
        //Given
        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        //When
        clientDbService.saveClient(client1);
        Integer clientId1 = client1.getClientId();

        //Then
        assertDoesNotThrow(() -> clientDbService.deleteClient(clientId1));
        assertThrows(ClientNotFoundException.class, () -> clientDbService.deleteClient(clientId1));
    }

    @Test
    void testGetClientNotFound() throws ClientNotFoundException {
        //Given
        Client client1 = new Client();
        client1.setFirstname("John");
        client1.setLastname("Smith");

        //When
        clientDbService.saveClient(client1);
        Integer clientId1 = client1.getClientId();
        Client savedClient = clientDbService.getClient(clientId1);
        clientDbService.deleteClient(clientId1);

        //Then
        assertNotNull(savedClient);
        assertThrows(ClientNotFoundException.class, () -> clientDbService.getClient(clientId1));
    }
}