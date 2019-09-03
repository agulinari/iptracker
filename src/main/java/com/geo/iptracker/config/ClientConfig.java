package com.geo.iptracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

@Configuration
public class ClientConfig {

    @Value("${ip2country.url}")
    private String ip2countryUrl;

    @Value("${restcountries.url}")
    private String restcountriesUrl;

    @Value("${fixer.url}")
    private String fixerUrl;

    @Bean(name = "ip2countryClient")
    public WebClient createaIp2CountryWebClient() {
        return WebClient.builder().baseUrl(ip2countryUrl)
                .filter(printlnFilter)
                .build();
    }

    ExchangeFilterFunction printlnFilter= new ExchangeFilterFunction() {
        @Override
        public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
            System.out.println("\n\n" + request.method().toString().toUpperCase() + ":\n\nURL:"
                    + request.url().toString() + ":\n\nHeaders:" + request.headers().toString() + "\n\nAttributes:"
                    + request.attributes() + "\n\n");

            return next.exchange(request);
        }
    };

    @Bean(name = "restcountriesClient")
    public WebClient createRestCountriesWebClient() {
        return WebClient.builder().baseUrl(restcountriesUrl)
                .build();
    }

    @Bean(name = "fixerClient")
    public WebClient createaFixerWebClient() {
        return WebClient.builder().baseUrl(fixerUrl)
                .build();
    }

}
