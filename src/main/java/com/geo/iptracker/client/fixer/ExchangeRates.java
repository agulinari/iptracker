package com.geo.iptracker.client.fixer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRates {

    private String base;
    private Map<String, Double> rates;

}
