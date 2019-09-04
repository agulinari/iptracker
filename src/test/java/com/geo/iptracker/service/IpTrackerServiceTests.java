package com.geo.iptracker.service;

import com.geo.iptracker.client.fixer.ExchangeRates;
import com.geo.iptracker.client.fixer.FixerClient;
import com.geo.iptracker.client.ip2country.Ip2CountryClient;
import com.geo.iptracker.client.ip2country.IpCountry;
import com.geo.iptracker.client.restcountries.Country;
import com.geo.iptracker.client.restcountries.RestCountriesClient;
import com.geo.iptracker.domain.dto.Currency;
import com.geo.iptracker.domain.dto.Distance;
import com.geo.iptracker.domain.dto.IpTrackerResponse;
import com.geo.iptracker.domain.dto.Language;
import com.geo.iptracker.service.impl.IpTrackerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class IpTrackerServiceTests {

    private IpTrackerService service;

    @MockBean
    private Ip2CountryClient ip2CountryClient;

    @MockBean
    private RestCountriesClient restCountriesClient;

    @MockBean
    private FixerClient fixerClient;

    @Before
    public void setUp() {
        service = new IpTrackerServiceImpl(ip2CountryClient, restCountriesClient, fixerClient);
    }

    @Test
    public void resolveIpOk() {

        String ip = "5.6.7.8";

        // Expected response
        Language lang = Language.builder().code("de").name("German").build();
        Currency currency = Currency.builder().code("EUR").name("Euro").symbol("€").rate(1.0).build();
        List<LocalDateTime> localTimes = List.of();
        List<Language> languages = List.of(lang);
        List<Currency> currencies = List.of(currency);

        Distance distance = Distance.builder()
                .kms(11566.193517623735)
                .latlng(List.of(51.0, 9.0))
                .build();
        IpTrackerResponse expected = IpTrackerResponse.builder()
                .ip(ip)
                .countryName("Germany")
                .isoCode("DE")
                .distanceToBsAs(distance)
                .date(LocalDateTime.now())
                .currencies(currencies)
                .languages(languages)
                .localTimes(localTimes)
                .build();

        // Mocks setup
        IpCountry ipCountry = IpCountry.builder().countryCode("DE").countryCode3("DEU").countryName("Germany").build();
        BDDMockito.when(ip2CountryClient.getCountryByIp(ip)).thenReturn(Mono.just(ipCountry));

        Country country = buildCountry();
        BDDMockito.when(restCountriesClient.getCountryByCode("DE")).thenReturn(Mono.just(country));

        Map<String, Double> ratesMap = Map.of("EUR", 1.0, "USD", 1.102943);
        ExchangeRates rates = ExchangeRates.builder().base("EUR").rates(ratesMap).build();
        BDDMockito.when(fixerClient.getExchangeRates()).thenReturn(Mono.just(rates));

        // Service call
        Mono<IpTrackerResponse> res = service.resolveIp(ip);

        // Verification
        StepVerifier.create(res)
                .assertNext(r -> {
                    assertEquals(r.getCountryName(), expected.getCountryName());
                    assertEquals(r.getIsoCode(), expected.getIsoCode());
                    assertEquals(r.getIp(), expected.getIp());
                    assertEquals(r.getCurrencies(), expected.getCurrencies());
                    assertEquals(r.getDistanceToBsAs(), expected.getDistanceToBsAs());
                    assertEquals(r.getLanguages(), expected.getLanguages());
                })
                .verifyComplete();

    }

    private Country buildCountry() {
        com.geo.iptracker.client.restcountries.Language lang =  com.geo.iptracker.client.restcountries.Language.builder()
                .iso639_1("de").name("German").build();
        com.geo.iptracker.client.restcountries.Currency currency = com.geo.iptracker.client.restcountries.Currency.builder()
                .code("EUR").name("Euro").symbol("€").build();
        List< com.geo.iptracker.client.restcountries.Language> languages = List.of(lang);
        List<com.geo.iptracker.client.restcountries.Currency> currencies = List.of(currency);

        Country country = Country.builder()
                .alpha2Code("DE")
                .alpha3Code("DEU")
                .name("Germany")
                .currencies(currencies)
                .languages(languages)
                .latlng(List.of(51.0, 9.0))
                .timezones(List.of("UTC+01:00"))
                .build();
        return country;
    }

}
