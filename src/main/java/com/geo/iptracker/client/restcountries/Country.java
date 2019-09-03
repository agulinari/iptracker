package com.geo.iptracker.client.restcountries;


import lombok.Data;

import java.util.List;

@Data
public class Country {

    private String name;
    private String alpha2Code;
    private String alpha3Code;
    private List<String> timezones;
    private List<Double> latlng;
    private List<Currency> currencies;
    private List<Language> languages;
}
