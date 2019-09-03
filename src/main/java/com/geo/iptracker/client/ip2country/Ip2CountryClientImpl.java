package com.geo.iptracker.client.ip2country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class Ip2CountryClientImpl implements Ip2CountryClient {

    @Autowired
    @Qualifier("ip2countryClient")
    private WebClient webClient;

    @Override
    public Mono<IpCountry> getCountryByIp(String ip) {

       return this.webClient.get()
               .uri(uriBuilder -> uriBuilder.path("/ip").query(ip)
                       .build())
               .retrieve()
               .bodyToMono(IpCountry.class);
    }
}

