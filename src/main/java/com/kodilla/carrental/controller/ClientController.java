package com.kodilla.carrental.controller;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.exception.ClientNotFoundException;
import com.kodilla.carrental.mapper.ClientMapper;
import com.kodilla.carrental.service.ClientDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientMapper clientMapper;
    private final ClientDbService clientDbService;

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<Client> clients = clientDbService.getAllClients();
        return ResponseEntity.ok(clientMapper.mapToClientDtoList(clients));
    }

    @GetMapping(value = "/{clientId}")
    public ResponseEntity<ClientDto> getClient(@PathVariable Integer clientId) throws ClientNotFoundException {
        return ResponseEntity.ok(clientMapper.mapToClientDto(clientDbService.getClient(clientId)));
    }

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        Client client = clientMapper.mapToClient(clientDto);
        Client savedClient = clientDbService.saveClient(client);
        return ResponseEntity.ok(clientMapper.mapToClientDto(savedClient));
    }

    @PutMapping
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) {
        Client client = clientMapper.mapToClient(clientDto);
        Client savedClient = clientDbService.saveClient(client);
        return ResponseEntity.ok(clientMapper.mapToClientDto(savedClient));
    }

    @DeleteMapping(value = "/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer clientId) throws ClientNotFoundException {
        clientDbService.deleteClient(clientId);
        return ResponseEntity.ok().build();
    }
}
