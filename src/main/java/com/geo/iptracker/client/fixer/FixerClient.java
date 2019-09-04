package com.geo.iptracker.client.fixer;

import reactor.core.publisher.Mono;

/**
 * HttpClient to consume data.io.fixer api
 */
public interface FixerClient {

    /**
     * Retrieve information about currency exchange rates
     * @return Exchange rates of world currencies
     */
    Mono<ExchangeRates> getExchangeRates();

}
