package com.kodilla.carrental.client;

import com.kodilla.carrental.domain.GasPricesListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class GasPriceClient {

    private final RestTemplate restTemplate;
    @Value("${gasprice.api.endpoint}")
    private String gasPriceApiEndpoint;
    @Value("${gasprice.api.token}")
    private String gasPriceApiToken;

    public GasPricesListDto getGasPrices() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("authorization", gasPriceApiToken);
        headers.set("content-type", "application/json");

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<GasPricesListDto> response = restTemplate.exchange(gasPriceApiEndpoint, HttpMethod.GET, entity, GasPricesListDto.class);

        return response.getBody();
    }

    public Double getGasolinePriceInPoland() {
        GasPricesListDto gasPrices = getGasPrices();

        Double price = Arrays.stream(gasPrices.getGasPrices())
                .filter(gp -> gp.getCountry().equals("Poland"))
                .map(gp -> gp.getGasoline())
                .mapToDouble(s -> Double.parseDouble(s.replace(',','.')))
                .average().orElse(0.0);

        return price;
    }
}
