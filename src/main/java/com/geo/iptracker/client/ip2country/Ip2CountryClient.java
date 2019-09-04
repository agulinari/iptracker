package com.geo.iptracker.client.ip2country;

import reactor.core.publisher.Mono;

/**
 * HttpClient to consume ip2country api
 */
public interface Ip2CountryClient {

    /**
     * Map an ip to a country
     * @param ip
     * @return country
     */
    Mono<IpCountry> getCountryByIp(String ip);

}
