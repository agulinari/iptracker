package com.geo.iptracker.service.impl;

import com.geo.iptracker.client.fixer.ExchangeRates;
import com.geo.iptracker.client.fixer.FixerClient;
import com.geo.iptracker.client.ip2country.Ip2CountryClient;
import com.geo.iptracker.client.restcountries.Country;
import com.geo.iptracker.client.restcountries.RestCountriesClient;
import com.geo.iptracker.domain.dto.*;
import com.geo.iptracker.service.IpTrackerService;
import com.geo.iptracker.util.IpTrackerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IpTrackerServiceImpl implements IpTrackerService {

    private static double BS_AS_LAT = -34.61315;
    private static double BS_AS_LONG = -58.37723;
    private static String USD_CODE = "USD";

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
        List<LocalHour> localTimes = calculateLocalTimes(country.getTimezones());

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

    private List<LocalHour> calculateLocalTimes(List<String> timezones) {
        return timezones.stream().map(this::getDateTime).collect(Collectors.toList());
    }

    private LocalHour getDateTime(String timezone) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        String prefix = timezone.substring(0,3);
        String offset = timezone.substring(3);
        if (!offset.isEmpty()) {
            ZoneOffset zoneOffset = ZoneOffset.of(offset);
            String hour = LocalDateTime.now(ZoneId.ofOffset(prefix, zoneOffset)).format(dtf);
            return LocalHour.builder().hour(hour).timezone(timezone).build();
        } else {
            String hour = LocalDateTime.now(ZoneOffset.UTC).format(dtf);
            return LocalHour.builder().hour(hour).timezone(timezone).build();
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
        Double rateUsd = exchangeRates.getRates().get(USD_CODE);
        Double rateBaseEur = exchangeRates.getRates().get(c.getCode());
        Double rateBaseUsd = null;
        if (rateBaseEur != null) {
            double div = rateBaseEur.doubleValue() / rateUsd.doubleValue();
            rateBaseUsd = BigDecimal.valueOf(div).setScale(2, RoundingMode.FLOOR).doubleValue();
        }
        Currency currency = Currency.builder()
                .code(c.getCode())
                .name(c.getName())
                .symbol(c.getSymbol())
                .usdRate(rateBaseUsd)
                .build();
        return currency;
    }

    private Distance calculateDistanceToBsAs(Double latitude, Double longitude) {
        double distance = IpTrackerUtil.distance(latitude, BS_AS_LAT, longitude, BS_AS_LONG, 0.0, 0.0);
        List<Double> latlng = List.of(latitude, longitude);
        return Distance.builder()
                .kms(BigDecimal.valueOf(distance).setScale(2, RoundingMode.FLOOR).doubleValue())
                .latlng(latlng)
                .build();

    }
}
