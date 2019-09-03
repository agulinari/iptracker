package com.geo.iptracker.client.ip2country;

import reactor.core.publisher.Mono;

public interface Ip2CountryClient {

    Mono<IpCountry> getCountryByIp(String ip);

}
