package com.geo.iptracker.service;

import com.geo.iptracker.domain.dto.IpTrackerResponse;
import reactor.core.publisher.Mono;

/**
 * Service to get a country from an ip address
 */
public interface IpTrackerService {

    /**
     * Track the ip address and return info about the country
     * @param ip the ip address to track.
     * @return the information of the country.
     */
    Mono<IpTrackerResponse> resolveIp(String ip);

}
