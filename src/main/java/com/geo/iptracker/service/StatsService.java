package com.geo.iptracker.service;

import com.geo.iptracker.domain.dto.CountryDistance;
import com.geo.iptracker.domain.dto.IpTrackerResponse;
import reactor.core.publisher.Mono;

/**
 * Service to collect statistics about ip tracker usage
 */
public interface StatsService {

    /**
     * Save stats: country distance to BsAs and #calls
     * @param r Country information.
     * @return Country information.
     */
    Mono<IpTrackerResponse> collectStats(IpTrackerResponse r);

    /**
     * Get the country farthest form BsAs
     * @return Country with max distance to BsAs
     */
    Mono<CountryDistance> getCountryWithMaxDistance();

    /**
     * Get the country closest to BsAs
     * @return Country with min distance to BsAs
     */
    Mono<CountryDistance> getCountryWithMinDistance();

    /**
     * Calculates average distances from all countries to BsAs
     * @return Average distance to BsAs
     */
    Mono<Double> getAverageDistance();

}
