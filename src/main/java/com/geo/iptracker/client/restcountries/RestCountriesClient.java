package com.geo.iptracker.client.restcountries;

import reactor.core.publisher.Mono;

public interface RestCountriesClient {

   Mono<Country> getCountryByCode(String code);

}