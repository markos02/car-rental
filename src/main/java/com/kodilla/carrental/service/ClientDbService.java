package com.kodilla.carrental.service;

import com.kodilla.carrental.domain.Client;
import com.kodilla.carrental.exception.ClientNotFoundException;
import com.kodilla.carrental.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientDbService {

    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClient(Integer clientId) throws ClientNotFoundException {
        return clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Integer clientId) throws ClientNotFoundException {
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
        } else {
            throw new ClientNotFoundException();
        }
    }
}
