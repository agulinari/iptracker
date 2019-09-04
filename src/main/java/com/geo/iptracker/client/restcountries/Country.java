package com.geo.iptracker.client.restcountries;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    private String name;
    private String alpha2Code;
    private String alpha3Code;
    private List<String> timezones;
    private List<Double> latlng;
    private List<Currency> currencies;
    private List<Language> languages;
}
