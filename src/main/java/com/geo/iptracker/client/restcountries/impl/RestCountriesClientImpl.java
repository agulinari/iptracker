package com.geo.iptracker.client.restcountries.impl;

import com.geo.iptracker.client.restcountries.Country;
import com.geo.iptracker.client.restcountries.RestCountriesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RestCountriesClientImpl implements RestCountriesClient {

    @Autowired
    @Qualifier("restcountriesClient")
    private WebClient webClient;

    @Override
    public Mono<Country> getCountryByCode(String code) {

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/alpha/"+code)
                        .query("fields=name;alpha2Code;alpha3Code;timezones;latlng;currencies;languages")
                        .build())
                .retrieve()
                .bodyToMono(Country.class);
    }

}
