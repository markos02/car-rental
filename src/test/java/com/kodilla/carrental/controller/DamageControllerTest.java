package com.kodilla.carrental.controller;

import com.google.gson.*;
import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.DamageDto;
import com.kodilla.carrental.domain.Rental;
import com.kodilla.carrental.mapper.DamageMapper;
import com.kodilla.carrental.service.DamageDbService;
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
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class DamageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DamageDbService damageDbService;

    @MockBean
    private DamageMapper damageMapper;

    @Test
    void testGetCarDamages() throws Exception {
        //Given
        Integer carId = 20;

        List<Damage> damages = List.of(
                new Damage(1, LocalDate.of(2023, 04, 11), new Car(), new Rental(), "Broken lamp"),
                new Damage(2, LocalDate.of(2023, 04, 11), new Car(), new Rental(), "Flat tire")
        );

        List<DamageDto> damagesDtos = List.of(
                new DamageDto(1, LocalDate.of(2023, 04, 11), carId, 24, "Broken lamp"),
                new DamageDto(2, LocalDate.of(2023, 04, 11), carId, 24, "Flat tire")
        );

        when(damageDbService.getCarDamages(carId)).thenReturn(damages);
        when(damageMapper.mapToDamageDtoList(damages)).thenReturn(damagesDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/damages/20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].damageId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is("2023-04-11")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carId", Matchers.is(carId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalId", Matchers.is(24)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("Broken lamp")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].damageId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].date", Matchers.is("2023-04-11")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carId", Matchers.is(carId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rentalId", Matchers.is(24)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", Matchers.is("Flat tire")));
    }
}