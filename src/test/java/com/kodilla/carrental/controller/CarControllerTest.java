package com.kodilla.carrental.controller;

import com.google.gson.Gson;
import com.kodilla.carrental.domain.Car;
import com.kodilla.carrental.domain.CarDto;
import com.kodilla.carrental.domain.CreateCarDto;
import com.kodilla.carrental.domain.enums.FuelType;
import com.kodilla.carrental.domain.enums.Transmission;
import com.kodilla.carrental.exception.CarGroupNotFoundException;
import com.kodilla.carrental.exception.CarNotFoundException;
import com.kodilla.carrental.mapper.CarMapper;
import com.kodilla.carrental.service.CarDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarDbService carDbService;

    @MockBean
    private CarMapper carMapper;

    @Test
    void testGetAllCarsNoCars() throws Exception {
        //Given
        when(carDbService.getAllCars()).thenReturn(List.of());

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void testGetAllCars() throws Exception {
        //Given
        List<Car> cars = List.of(
                new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1"),
                new Car("ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2")
        );

        List<CarDto> carDtos = List.of(
                new CarDto(1, 1, new ArrayList<>(), new ArrayList<>(), "ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1"),
                new CarDto(2, 2, new ArrayList<>(), new ArrayList<>(), "ABC 5678", FuelType.GASOLINE, Transmission.MANUAL, "Test model 2")
        );

        when(carDbService.getAllCars()).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/cars")
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
    void testGetCar() throws Exception {
        //Given
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        CarDto carDto1 = new CarDto(1, 1, new ArrayList<>(), new ArrayList<>(), "ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        when(carDbService.getCar(1)).thenReturn(car1);
        when(carMapper.mapToCarDto(car1)).thenReturn(carDto1);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/cars/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate", Matchers.is("ABC 1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fuelType", Matchers.is("GASOLINE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model", Matchers.is("Test model 1")));
    }

    @Test
    void testCreateCar() throws Exception {
        //Given
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        CreateCarDto createCarDto = new CreateCarDto(1, "ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        CarDto createdCarDto = new CarDto(1, 1, new ArrayList<>(), new ArrayList<>(), "ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        when(carMapper.mapToCreateCar(createCarDto)).thenReturn(car1);
        when(carDbService.createNewCar(any(), eq(1))).thenReturn(car1);
        when(carMapper.mapToCarDto(car1)).thenReturn(createdCarDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createCarDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate", Matchers.is("ABC 1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fuelType", Matchers.is("GASOLINE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model", Matchers.is("Test model 1")));
    }

    @Test
    void testUpdateCar() throws Exception {
        //Given
        Car car1 = new Car("ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        CarDto carDto = new CarDto(1, 1, new ArrayList<>(), new ArrayList<>(), "ABC 1234", FuelType.GASOLINE, Transmission.AUTOMATIC, "Test model 1");

        when(carMapper.mapToCar(carDto)).thenReturn(car1);
        when(carDbService.saveCar(car1)).thenReturn(car1);
        when(carMapper.mapToCarDto(any())).thenReturn(carDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(carDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carGroupId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate", Matchers.is("ABC 1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fuelType", Matchers.is("GASOLINE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model", Matchers.is("Test model 1")));
    }

    @Test
    void testCheckIfAvailable() throws Exception {
        //Given
        String message = "Hurray! Car with given ID is available in given period";

        Integer carId = 1;
        LocalDate dateFrom = LocalDate.now().plusDays(10);
        LocalDate dateTo = LocalDate.now().plusDays(15);

        when(carDbService.checkIfAvailable(carId, dateFrom, dateTo)).thenReturn(true);

        StringBuilder sb = new StringBuilder();
        sb.append("/v1/cars/isAvailable/");
        sb.append(carId);
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
                .andExpect(MockMvcResultMatchers.content().string(message));
    }

    @Test
    void testGetAllAvailable() throws Exception {
        //Given
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

        when(carDbService.getAllAvailable(dateFrom, dateTo)).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        StringBuilder sb = new StringBuilder();
        sb.append("/v1/cars/allAvailable/");
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