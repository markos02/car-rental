package com.kodilla.carrental.controller;

import com.kodilla.carrental.domain.*;
import com.kodilla.carrental.domain.enums.OrderStatus;
import com.kodilla.carrental.domain.enums.RentalStatus;
import com.kodilla.carrental.mapper.OrderMapper;
import com.kodilla.carrental.mapper.RentalMapper;
import com.kodilla.carrental.service.OrderDbService;
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

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderDbService orderDbService;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private RentalMapper rentalMapper;

    @Test
    void TestGetAllOrders() throws Exception {
        //Given
        LocalDate from = LocalDate.now().plusDays(1);
        LocalDate to = LocalDate.now().plusDays(10);

        List<Order> orders = List.of(
                new Order(1, new Client(), new Rental(), new Car(), from, to, OrderStatus.IN_PROCESS, true, true, true, 0.5),
                new Order(2, new Client(), new Rental(), new Car(), from, to, OrderStatus.COMPLETED, false, false, false, 0.4)
        );

        OrderDto orderDto1 = new OrderDto.OrderDtoBuilder()
                .orderId(1)
                .clientId(10)
                .rentalId(11)
                .carId(12)
                .dateFrom(from)
                .dateTo(to)
                .status(OrderStatus.IN_PROCESS)
                .childSeat(true)
                .gps(true)
                .extraDriver(true)
                .fuelLevel(0.5)
                .build();

        OrderDto orderDto2 = new OrderDto.OrderDtoBuilder()
                .orderId(2)
                .clientId(20)
                .rentalId(21)
                .carId(22)
                .dateFrom(from)
                .dateTo(to)
                .status(OrderStatus.COMPLETED)
                .childSeat(false)
                .gps(false)
                .extraDriver(false)
                .fuelLevel(0.4)
                .build();

        List<OrderDto> ordersDtos = List.of(orderDto1, orderDto2);

        when(orderDbService.getAllOrders()).thenReturn(orders);
        when(orderMapper.mapToOrderDtoList(orders)).thenReturn(ordersDtos);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rentalId", Matchers.is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].carId", Matchers.is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateFrom", Matchers.is(from.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateTo", Matchers.is(to.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(OrderStatus.IN_PROCESS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].childSeat", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gps", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].extraDriver", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fuelLevel", Matchers.is(0.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientId", Matchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rentalId", Matchers.is(21)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].carId", Matchers.is(22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateFrom", Matchers.is(from.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateTo", Matchers.is(to.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status", Matchers.is(OrderStatus.COMPLETED.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].childSeat", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gps", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].extraDriver", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fuelLevel", Matchers.is(0.4)));
    }

    @Test
    void TestGetOrder() throws Exception {
        //Given
        LocalDate from = LocalDate.now().plusDays(1);
        LocalDate to = LocalDate.now().plusDays(10);
        Integer orderId = 1;

        Order order = new Order(orderId, new Client(), new Rental(), new Car(), from, to, OrderStatus.IN_PROCESS, true, true, true, 0.5);

        OrderDto orderDto = new OrderDto.OrderDtoBuilder()
                .orderId(1)
                .clientId(10)
                .rentalId(11)
                .carId(12)
                .dateFrom(from)
                .dateTo(to)
                .status(OrderStatus.IN_PROCESS)
                .childSeat(true)
                .gps(true)
                .extraDriver(true)
                .fuelLevel(0.5)
                .build();

        when(orderDbService.getOrder(orderId)).thenReturn(order);
        when(orderMapper.mapToOrderDto(order)).thenReturn(orderDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalId", Matchers.is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carId", Matchers.is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateFrom", Matchers.is(from.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateTo", Matchers.is(to.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(OrderStatus.IN_PROCESS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.childSeat", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gps", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.extraDriver", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fuelLevel", Matchers.is(0.5)));
    }

    @Test
    void TestStartRental() throws Exception {
        //Given
        Integer orderId = 1;
        Rental rental = new Rental(1, new Order(), new ArrayList<>(), RentalStatus.RENTED);
        RentalDto rentalDto = new RentalDto(1, 2, new ArrayList<>(), RentalStatus.RENTED);

        when(orderDbService.startRental(orderId)).thenReturn(rental);
        when(rentalMapper.mapToRentalDto(rental)).thenReturn(rentalDto);

        //When
        //Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.damagesIds", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(RentalStatus.RENTED.toString())));
    }
}