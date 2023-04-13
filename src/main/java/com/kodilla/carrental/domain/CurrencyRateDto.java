package com.kodilla.carrental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRateDto {

    @JsonProperty("table")
    String table;

    @JsonProperty("currency")
    String currency;

    @JsonProperty("code")
    String code;

    @JsonProperty("rates")
    RatesDto[] rates;

    public Double getCurrentRate() {
        return rates[0].getRate();
    }
}
