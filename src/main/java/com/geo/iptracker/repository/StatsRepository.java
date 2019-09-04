package com.geo.iptracker.repository;

import org.springframework.data.redis.core.ZSetOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository to store information about IpTracker service usage
 */
public interface StatsRepository {

    /**
     * Insert information about country distance to BsAs and increments #calls from that country
     * @param countryCode country ISO code
     * @param countryName country name
     * @param distance in kms
     * @return #calls from country
     */
    Mono<Double> insertStat(String countryCode, String countryName, Double distance);

    /**
     * Get country with max distance to BsAs
     * @return country name and distance
     */
    Mono<ZSetOperations.TypedTuple<String>> getMaxDistance();

    /**
     * Get country with min distance to BsAs
     * @return country name and distance
     */
    Mono<ZSetOperations.TypedTuple<String>> getMinDistance();

    /**
     * Get countries and distances to BsAs
     * @return
     */
    Flux<ZSetOperations.TypedTuple<String>> getDistances();

    /**
     * Get countries and #calls
     * @return
     */
    Flux<ZSetOperations.TypedTuple<String>> getCalls();

}
