package com.kodilla.carrental.controller;

import com.google.gson.Gson;
import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.CarDto;
import com.kodilla.carrental.domain.CarGroup;
import com.kodilla.carrental.domain.CarGroupDto;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.mapper.CarGroupMapper;
import com.kodilla.carrental.mapper.CarMapper;
import com.kodilla.carrental.service.CarDbService;
import com.kodilla.carrental.service.CarGroupDbService;
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
class CarGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarGroupDbService carGroupDbService;

    @MockBean
    private CarGroupMapper carGroupMapper;

    @MockBean
    private CarMapper carMapper;

    @Test
    void testGetAllCarGroups() throws Exception {
        //Given
        List<CarGroup> carGroups = List.of(
                new CarGroup("Economy"),
                new CarGroup("Regular"),
                new CarGroup("Premium")
        );

        List<CarGroupDto> carGroupsDtos = List.of(
                new CarGroupDto(1, "Economy", new ArrayList<>()),
                new CarGroupDto(2, "Regular", new ArrayList<>()),
                new CarGroupDto(3, "Premium", new ArrayList<>())
        );

        when(carGroupDbService.getAllCarGroups()).thenReturn(carGroups);
        when(carGroupMapper.mapToCarGroupDtoList(carGroups)).thenReturn(carGroupsDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/car_groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Economy")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carsIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carGroupId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Regular")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carsIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].carGroupId", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", Matchers.is("Premium")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].carsIds", Matchers.hasSize(0)));
    }

    @Test
    void testGetCarGroup() throws Exception {
        //Given
        CarGroup carGroup = new CarGroup("Economy");

        CarGroupDto carGroupsDtos = new CarGroupDto(1, "Economy", new ArrayList<>());

        when(carGroupDbService.getCarGroup(1)).thenReturn(carGroup);
        when(carGroupMapper.mapToCarGroupDto(carGroup)).thenReturn(carGroupsDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/car_groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Economy")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carsIds", Matchers.hasSize(0)));
    }

    @Test
    void testCreateCarGroup() throws Exception {
        //Given
        CarGroup carGroup = new CarGroup(1, "Economy", new ArrayList<>());

        CarGroupDto carGroupDto = new CarGroupDto(1, "Economy", new ArrayList<>());

        when(carGroupMapper.mapToCarGroup(any())).thenReturn(carGroup);
        when(carGroupDbService.saveCarGroup(carGroup)).thenReturn(carGroup);
        when(carGroupMapper.mapToCarGroupDto(carGroup)).thenReturn(carGroupDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(carGroupDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/car_groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Economy")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carsIds", Matchers.hasSize(0)));
    }

    @Test
    void tesUpdateCarGroup() throws Exception {
        //Given
        CarGroup carGroup = new CarGroup(1, "Economy", new ArrayList<>());

        CarGroupDto carGroupDto = new CarGroupDto(1, "Economy", new ArrayList<>());

        when(carGroupMapper.mapToCarGroup(any())).thenReturn(carGroup);
        when(carGroupDbService.saveCarGroup(carGroup)).thenReturn(carGroup);
        when(carGroupMapper.mapToCarGroupDto(carGroup)).thenReturn(carGroupDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(carGroupDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/car_groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Economy")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carsIds", Matchers.hasSize(0)));
    }

    @Test
    void testGetAllCarsInGroup() throws Exception {
        //Given
        Integer groupId = 1;

        List<Car> cars = List.of(
                new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1"),
                new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2")
        );

        List<CarDto> carDtos = List.of(
                new CarDto(1, 1, new ArrayList<>(), new ArrayList<>(), "ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1"),
                new CarDto(2, 2, new ArrayList<>(), new ArrayList<>(), "ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2")
        );

        when(carGroupDbService.getAllCarsInGroup(groupId)).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        StringBuilder sb = new StringBuilder();
        sb.append("/v1/car_groups/");
        sb.append(groupId);
        sb.append("/cars");
        String url = sb.toString();

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ordersIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].licensePlate", Matchers.is("ABC 1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fuelType", Matchers.is("GASOLINE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("Test model 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carGroupId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ordersIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].licensePlate", Matchers.is("ABC 5678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fuelType", Matchers.is("GASOLINE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model", Matchers.is("Test model 2")));
    }

    @Test
    void testGetAllAvailable() throws Exception {
        //Given
        Integer groupId = 1;

        List<Car> cars = List.of(
                new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1"),
                new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2")
        );

        List<CarDto> carDtos = List.of(
                new CarDto(1, 1, new ArrayList<>(), new ArrayList<>(), "ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1"),
                new CarDto(2, 2, new ArrayList<>(), new ArrayList<>(), "ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2")
        );

        LocalDate dateFrom = LocalDate.now().plusDays(10);
        LocalDate dateTo = LocalDate.now().plusDays(15);

        when(carGroupDbService.getAllAvailableGroup(groupId, dateFrom, dateTo)).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        StringBuilder sb = new StringBuilder();
        sb.append("/v1/car_groups/");
        sb.append(groupId);
        sb.append("/");
        sb.append(dateFrom);
        sb.append("/");
        sb.append(dateTo);
        String url = sb.toString();

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ordersIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].licensePlate", Matchers.is("ABC 1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fuelType", Matchers.is("GASOLINE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("Test model 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carGroupId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ordersIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].licensePlate", Matchers.is("ABC 5678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fuelType", Matchers.is("GASOLINE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model", Matchers.is("Test model 2")));
    }
}