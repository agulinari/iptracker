package com.geo.iptracker.client.fixer.impl;

import com.geo.iptracker.client.fixer.ExchangeRates;
import com.geo.iptracker.client.fixer.FixerClient;
import com.geo.iptracker.client.ip2country.IpCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FixerClientImpl implements FixerClient {

    @Value("${fixer.apikey}")
    private String apikey;

    @Autowired
    @Qualifier("fixerClient")
    private WebClient webClient;

    @Override
    public Mono<ExchangeRates> getExchangeRates() {

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("access_key", apikey)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRates.class);
    }


}
