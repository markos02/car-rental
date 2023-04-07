package com.kodilla.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ClientDto {

    private Integer clientId;
    private String firstname;
    private String lastname;
    private List<Integer> ordersIds;
}
