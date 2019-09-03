package com.geo.iptracker.client.fixer;

import reactor.core.publisher.Mono;

public interface FixerClient {

    Mono<ExchangeRates> getExchangeRates();

}
