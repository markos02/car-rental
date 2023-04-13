package com.kodilla.carrental.client;

import com.kodilla.carrental.domain.CurrencyRateDto;
import com.kodilla.carrental.domain.GasPriceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NbpClientTest {

    @Autowired
    NbpClient nbpClient;

    @Test
    void TestGetTrelloBoards() {
        //Given
        //When
        CurrencyRateDto response = nbpClient.getEuroExchangeRate();
        Double currentRate = response.getCurrentRate();

        //Then
        assertNotNull(currentRate);
        assertNotEquals(0.0,currentRate);
    }
}