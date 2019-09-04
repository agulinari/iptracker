package com.geo.iptracker.controller;

import com.geo.iptracker.domain.dto.CountryDistance;
import com.geo.iptracker.service.StatsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
public class StatsControllerTests {

    private WebTestClient client;

    @MockBean
    private StatsService statsService;

    @Before
    public void setUp() {
        client = WebTestClient.bindToController(
                new StatsController(statsService))
                //.controllerAdvice(RestExceptionHandler.class)
                .build();
    }


    @Test
    public void getCountryWithMaxDistance() {

        CountryDistance country = CountryDistance.builder().countryName("China").distance(20000.00).build();
        BDDMockito.when(statsService.getCountryWithMaxDistance()).thenReturn(Mono.just(country));


        client.get().uri("/api/stats/max")
                .exchange().expectStatus().isOk().expectBody(CountryDistance.class)
                .equals(country);
    }

    @Test
    public void getCountryWithMinDistance() {

        CountryDistance country = CountryDistance.builder().countryName("Argentina").distance(500.00).build();
        BDDMockito.when(statsService.getCountryWithMinDistance()).thenReturn(Mono.just(country));


        client.get().uri("/api/stats/min")
                .exchange().expectStatus().isOk().expectBody(CountryDistance.class)
                .equals(country);
    }

    @Test
    public void getAverageDistance() {

        Double average = 25000.00;
        BDDMockito.when(statsService.getAverageDistance()).thenReturn(Mono.just(average));


        client.get().uri("/api/stats/average")
                .exchange().expectStatus().isOk().expectBody(Double.class)
                .equals(average);
    }

}
