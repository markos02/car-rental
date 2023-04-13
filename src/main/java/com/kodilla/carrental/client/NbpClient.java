package com.kodilla.carrental.client;

import com.kodilla.carrental.domain.CurrencyRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NbpClient {

    private final RestTemplate restTemplate;

    private final String REQUEST = "http://api.nbp.pl/api/exchangerates/rates/a/eur/";

    public CurrencyRateDto getEuroExchangeRate() {
        CurrencyRateDto response = restTemplate.getForObject(REQUEST, CurrencyRateDto.class);
        return response;
    }
}
