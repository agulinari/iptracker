package com.geo.iptracker.controller;

import com.geo.iptracker.domain.dto.*;
import com.geo.iptracker.service.IpTrackerService;
import com.geo.iptracker.service.StatsService;
import net.bytebuddy.asm.Advice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class IpTrackerControllerTests {


    private WebTestClient client;

    @MockBean
    private IpTrackerService ipTrackerService;

    @MockBean
    private StatsService statsService;

    @Before
    public void setUp() {
        client = WebTestClient.bindToController(
                new IpTrackerController(ipTrackerService, statsService))
                //.controllerAdvice(RestExceptionHandler.class)
                .build();
    }


    @Test
    public void resolveIpOk() {
        String ip = "5.6.7.8";

        Language lang = Language.builder().code("de").name("German").build();
        Currency currency = Currency.builder().code("EUR").name("Euro").symbol("€").usdRate(1.0).build();
        List<LocalHour> localTimes = List.of();
        List<Language> languages = List.of(lang);
        List<Currency> currencies = List.of(currency);

        Distance distance = Distance.builder()
                .kms(11566.19)
                .latlng(List.of(51.0, 9.0))
                .build();
        IpTrackerResponse response = IpTrackerResponse.builder()
                .ip(ip)
                .countryName("Germany")
                .isoCode("DE")
                .distanceToBsAs(distance)
                .date(LocalDateTime.now())
                .currencies(currencies)
                .languages(languages)
                .localTimes(localTimes)
                .build();

        BDDMockito.when(ipTrackerService.resolveIp(ip)).thenReturn(Mono.just(response));
        BDDMockito.when(statsService.collectStats(response)).thenReturn(Mono.just(response));

        IpAddressRequest request = new IpAddressRequest(ip);

        client.post().uri("/api/ipaddress")
                .body(BodyInserters.fromObject(request))
                .exchange().expectStatus().isOk().expectBody(IpTrackerResponse.class)
                .equals(response);

        BDDMockito.verify(statsService, BDDMockito.times(1)).collectStats(response);
    }

    @Test
    public void invalidIp() {
        String ip = "12.r.2r.23";
        IpAddressRequest request = new IpAddressRequest(ip);
        client.post().uri("/api/ipaddress")
                .body(BodyInserters.fromObject(request))
                .exchange().expectStatus().isBadRequest();
    }
}
