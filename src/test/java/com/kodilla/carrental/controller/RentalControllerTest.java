package com.kodilla.carrental.controller;

import com.google.gson.Gson;
import com.kodilla.carrental.domain.Order;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.domain.RentalDto;
import com.kodilla.carrental.domain.ReturnCarDto;
import com.kodilla.carrental.domain.enums.RentalStatus;
import com.kodilla.carrental.exception.OrderNotFoundException;
import com.kodilla.carrental.exception.RentalNotFoundException;
import com.kodilla.carrental.mapper.CarGroupMapper;
import com.kodilla.carrental.mapper.RentalMapper;
import com.kodilla.carrental.service.CarGroupDbService;
import com.kodilla.carrental.service.RentalDbService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalDbService rentalDbService;

    @MockBean
    private RentalMapper rentalMapper;

    @Test
    void testGetAllRentals() throws Exception {
        //Given
        List<Rental> rentals = List.of(
                new Rental(1, new Order(), new ArrayList<>(), RentalStatus.RENTED),
                new Rental(2, new Order(), new ArrayList<>(), RentalStatus.ORDERED)
        );

        List<RentalDto> rentalsDtos = List.of(
                new RentalDto(1, 10, new ArrayList<>(), RentalStatus.RENTED),
                new RentalDto(2, 20, new ArrayList<>(), RentalStatus.ORDERED)
        );

        when(rentalDbService.getAllRentals()).thenReturn(rentals);
        when(rentalMapper.mapToRentalDtoList(rentals)).thenReturn(rentalsDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/rentals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(RentalStatus.RENTED.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rentalId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderId", Matchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status", Matchers.is(RentalStatus.ORDERED.toString())));
    }

    @Test
    void testGetActiveRentals() throws Exception {
        //Given
        List<Rental> rentals = List.of(
                new Rental(1, new Order(), new ArrayList<>(), RentalStatus.RENTED),
                new Rental(2, new Order(), new ArrayList<>(), RentalStatus.ORDERED)
        );

        List<RentalDto> rentalsDtos = List.of(
                new RentalDto(1, 10, new ArrayList<>(), RentalStatus.RENTED),
                new RentalDto(2, 20, new ArrayList<>(), RentalStatus.ORDERED)
        );

        when(rentalDbService.getActiveRentals()).thenReturn(rentals);
        when(rentalMapper.mapToRentalDtoList(rentals)).thenReturn(rentalsDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/rentals/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(RentalStatus.RENTED.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rentalId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderId", Matchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status", Matchers.is(RentalStatus.ORDERED.toString())));
    }

    @Test
    void testGetOverdueRentals() throws Exception {
        //Given
        List<Rental> rentals = List.of(
                new Rental(1, new Order(), new ArrayList<>(), RentalStatus.RENTED),
                new Rental(2, new Order(), new ArrayList<>(), RentalStatus.ORDERED)
        );

        List<RentalDto> rentalsDtos = List.of(
                new RentalDto(1, 10, new ArrayList<>(), RentalStatus.RENTED),
                new RentalDto(2, 20, new ArrayList<>(), RentalStatus.ORDERED)
        );

        when(rentalDbService.getOverdueRentals()).thenReturn(rentals);
        when(rentalMapper.mapToRentalDtoList(rentals)).thenReturn(rentalsDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/rentals/overdue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(RentalStatus.RENTED.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rentalId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderId", Matchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status", Matchers.is(RentalStatus.ORDERED.toString())));
    }

    @Test
    void testGetRental() throws Exception {
        //Given
        Integer rentalId = 1;

        Rental rental = new Rental(rentalId, new Order(), new ArrayList<>(), RentalStatus.RENTED);
        RentalDto rentalDto = new RentalDto(rentalId, 10, new ArrayList<>(), RentalStatus.RENTED);

        when(rentalDbService.getRental(rentalId)).thenReturn(rental);
        when(rentalMapper.mapToRentalDto(rental)).thenReturn(rentalDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/rentals/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(RentalStatus.RENTED.toString())));
    }

    @Test
    void testCreateRental() throws Exception {
        //Given
        Integer rentalId = 1;

        Rental rental = new Rental(rentalId, new Order(), new ArrayList<>(), RentalStatus.RENTED);
        RentalDto rentalDto = new RentalDto(rentalId, 10, new ArrayList<>(), RentalStatus.RENTED);

        when(rentalMapper.mapToRental(rentalDto)).thenReturn(rental);
        when(rentalDbService.saveRental(any())).thenReturn(rental);
        when(rentalMapper.mapToRentalDto(rental)).thenReturn(rentalDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(rentalDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(RentalStatus.RENTED.toString())));
    }

    @Test
    void testUpdateRental() throws Exception {
        //Given
        Integer rentalId = 1;

        Rental rental = new Rental(rentalId, new Order(), new ArrayList<>(), RentalStatus.RENTED);
        RentalDto rentalDto = new RentalDto(rentalId, 10, new ArrayList<>(), RentalStatus.RENTED);

        when(rentalMapper.mapToRental(rentalDto)).thenReturn(rental);
        when(rentalDbService.saveRental(any())).thenReturn(rental);
        when(rentalMapper.mapToRentalDto(rental)).thenReturn(rentalDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(rentalDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(RentalStatus.RENTED.toString())));
    }
}