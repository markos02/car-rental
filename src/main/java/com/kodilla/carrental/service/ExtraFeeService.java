package com.kodilla.carrental.service;

import com.kodilla.carrental.client.GasPriceClient;
import com.kodilla.carrental.client.NbpClient;
import com.kodilla.carrental.domain.Damage;
import com.kodilla.carrental.domain.Fee;
import com.kodilla.carrental.domain.FeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtraFeeService {

    private final GasPriceClient gasPriceClient;
    private final NbpClient nbpClient;
    private static final Double reparationCost = 500.0;
    private static final Double overdueCost = 100.0;
    private String description;
    private Double value;

    public Fee newDamagesFee(List<Damage> newDamages) {
        description = "Damage fee";
        value = newDamages.size() * reparationCost;
        return new Fee(description, value);
    }

    public Fee overdueFee(Long overdue) {
        description = "Overdue fee";
        value = overdue * overdueCost;
        return new Fee(description, value);
    }

    public Fee fuelFee(Double fuelDifference) {
        Double gasPriceEur = gasPriceClient.getGasolinePriceInPoland();
        Double exchangeRate = nbpClient.getEuroExchangeRate().getCurrentRate();
        description = "Fuel fee";
        value = fuelDifference * gasPriceEur * exchangeRate;
        return new Fee(description, value);
    }
}
