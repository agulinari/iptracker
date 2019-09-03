package com.geo.iptracker.client.fixer;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRates {

    private String base;
    private Map<String, Double> rates;

}
