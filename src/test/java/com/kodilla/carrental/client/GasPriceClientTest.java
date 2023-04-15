package com.kodilla.carrental.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GasPriceClientTest {

    @Autowired
    GasPriceClient gasPriceClient;

    @Test
    void getGasPrices() {
        //Given
        //When
        Double price = gasPriceClient.getGasolinePriceInPoland();

        //Then
        assertNotNull(price);
        assertNotEquals(0.0, price);
    }
}