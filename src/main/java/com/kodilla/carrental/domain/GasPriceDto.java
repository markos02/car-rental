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
public class GasPriceDto {

    @JsonProperty("currency")
    String currency;

    @JsonProperty("lpg")
    String lpg;

    @JsonProperty("diesel")
    String diesel;

    @JsonProperty("gasoline")
    String gasoline;

    @JsonProperty("country")
    String country;
}
