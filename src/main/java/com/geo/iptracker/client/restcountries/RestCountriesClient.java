package com.geo.iptracker.client.restcountries;

import reactor.core.publisher.Mono;

/**
 * HttpClient to consume restcountries api
 */
public interface RestCountriesClient {

   /**
    * Get country information searching by iso code
    * @param code country iso code
    * @return country information
    */
   Mono<Country> getCountryByCode(String code);

}