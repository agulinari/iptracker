package com.geo.iptracker.service.impl;

import com.geo.iptracker.client.fixer.ExchangeRates;
import com.geo.iptracker.client.fixer.FixerClient;
import com.geo.iptracker.client.ip2country.Ip2CountryClient;
import com.geo.iptracker.client.restcountries.Country;
import com.geo.iptracker.client.restcountries.RestCountriesClient;
import com.geo.iptracker.domain.dto.Currency;
import com.geo.iptracker.domain.dto.Distance;
import com.geo.iptracker.domain.dto.IpTrackerResponse;
import com.geo.iptracker.domain.dto.Language;
import com.geo.iptracker.service.IpTrackerService;
import com.geo.iptracker.util.IpTrackerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IpTrackerServiceImpl implements IpTrackerService {

    private static double BS_AS_LAT = -34.61315;
    private static double BS_AS_LONG = -58.37723;

    private final Ip2CountryClient ip2CountryClient;
    private final RestCountriesClient restCountriesClient;
    private final FixerClient fixerClient;

    @Autowired
    public IpTrackerServiceImpl(Ip2CountryClient ip2CountryClient, RestCountriesClient restCountriesClient, FixerClient fixerClient) {
        this.ip2CountryClient = ip2CountryClient;
        this.restCountriesClient = restCountriesClient;
        this.fixerClient = fixerClient;
    }

    @Override
    public Mono<IpTrackerResponse> resolveIp(String ip) {

        Mono<ExchangeRates> mRates = fixerClient.getExchangeRates();
        Mono<Country> mCountry = ip2CountryClient.getCountryByIp(ip)
                .flatMap(ipCountry -> restCountriesClient.getCountryByCode(ipCountry.getCountryCode()));
        return mCountry.zipWith(mRates, (country, exchangeRates) -> buildIpTrackerResponse(ip, country, exchangeRates));

    }

    private IpTrackerResponse buildIpTrackerResponse(String ip, Country country, ExchangeRates exchangeRates) {

        Distance distanceToBsAs = calculateDistanceToBsAs(country.getLatlng().get(0), country.getLatlng().get(1));
        List<Currency> currencies = buildCurrency(country.getCurrencies(), exchangeRates);
        List<Language> languages = buildLanguages(country.getLanguages());
        List<LocalDateTime> localTimes = calculateLocalTimes(country.getTimezones());

        return IpTrackerResponse.builder()
                .ip(ip)
                .date(LocalDateTime.now())
                .isoCode(country.getAlpha2Code())
                .languages(languages)
                .localTimes(localTimes)
                .countryName(country.getName())
                .distanceToBsAs(distanceToBsAs)
                .currencies(currencies)
        .build();
    }

    private List<LocalDateTime> calculateLocalTimes(List<String> timezones) {
        return timezones.stream().map(this::getDateTime).collect(Collectors.toList());
    }

    private LocalDateTime getDateTime(String timezone) {
        String prefix = timezone.substring(0,3);
        String offset = timezone.substring(3);
        if (!offset.isEmpty()) {
            ZoneOffset zoneOffset = ZoneOffset.of(offset);
            return LocalDateTime.now(ZoneId.ofOffset(prefix, zoneOffset));
        } else {
            return LocalDateTime.now(ZoneOffset.UTC);
        }
    }

    private List<Language> buildLanguages(List<com.geo.iptracker.client.restcountries.Language> languages) {
        return languages.stream().map(l -> Language.builder()
                .code(l.getIso639_1())
                .name(l.getName())
                .build()
        ).collect(Collectors.toList());
    }

    private List<Currency> buildCurrency(List<com.geo.iptracker.client.restcountries.Currency> currencies, ExchangeRates exchangeRates) {
        return currencies.stream().map(c -> mapCurrency(c, exchangeRates)).collect(Collectors.toList());
    }

    private Currency mapCurrency(com.geo.iptracker.client.restcountries.Currency c, ExchangeRates exchangeRates) {
        Double rate = exchangeRates.getRates().get(c.getCode());
        Currency currency = Currency.builder()
                .code(c.getCode())
                .name(c.getName())
                .symbol(c.getSymbol())
                .rate(rate)
                .build();
        return currency;
    }

    private Distance calculateDistanceToBsAs(Double latitude, Double longitude) {
        double distance = IpTrackerUtil.distance(latitude, BS_AS_LAT, longitude, BS_AS_LONG, 0.0, 0.0);
        List<Double> latlng = List.of(latitude, longitude);
        return Distance.builder()
                .kms(distance)
                .latlng(latlng)
                .build();

    }
}
