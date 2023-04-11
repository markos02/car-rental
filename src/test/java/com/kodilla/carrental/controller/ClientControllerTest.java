package com.kodilla.carrental.controller;

import com.google.gson.Gson;
import com.kodilla.carrental.domain.Client;
import com.kodilla.carrental.domain.ClientDto;
import com.kodilla.carrental.exception.ClientNotFoundException;
import com.kodilla.carrental.mapper.CarMapper;
import com.kodilla.carrental.mapper.ClientMapper;
import com.kodilla.carrental.service.CarDbService;
import com.kodilla.carrental.service.ClientDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientDbService clientDbService;

    @MockBean
    private ClientMapper clientMapper;

    @Test
    void testGetAllClients() throws Exception {
        //Given
        List<Client> clients = List.of(
                new Client(1, "John", "Smith", new ArrayList<>()),
                new Client(2, "Jane", "Doe", new ArrayList<>())
        );

        List<ClientDto> clientsDtos = List.of(
                new ClientDto(1, "John", "Smith", new ArrayList<>()),
                new ClientDto(2, "Jane", "Doe", new ArrayList<>())
        );

        when(clientDbService.getAllClients()).thenReturn(clients);
        when(clientMapper.mapToClientDtoList(clients)).thenReturn(clientsDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", Matchers.is("Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ordersIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstname", Matchers.is("Jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastname", Matchers.is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ordersIds", Matchers.hasSize(0)));
    }

    @Test
    void testGetClient() throws Exception {
        //Given
        Integer clientId = 1;
        Client client = new Client(1, "John", "Smith", new ArrayList<>());
        ClientDto clientDto = new ClientDto(1, "John", "Smith", new ArrayList<>());

        when(clientDbService.getClient(clientId)).thenReturn(client);
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds", Matchers.hasSize(0)));
    }

    @Test
    void testCreateClient() throws Exception {
        //Given
        Client client = new Client(1, "John", "Smith", new ArrayList<>());
        ClientDto clientDto = new ClientDto(1, "John", "Smith", new ArrayList<>());

        when(clientMapper.mapToClient(clientDto)).thenReturn(client);
        when(clientDbService.saveClient(any())).thenReturn(client);
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(clientDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds", Matchers.hasSize(0)));
    }

    @Test
    void testUpdateClient() throws Exception {
        //Given
        Client client = new Client(1, "John", "Smith", new ArrayList<>());
        ClientDto clientDto = new ClientDto(1, "John", "Smith", new ArrayList<>());

        when(clientMapper.mapToClient(clientDto)).thenReturn(client);
        when(clientDbService.saveClient(any())).thenReturn(client);
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(clientDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds", Matchers.hasSize(0)));
    }
}