package com.geo.iptracker.client.restcountries;

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
                        .build())
                .retrieve()
                .bodyToMono(Country.class);
    }

}
