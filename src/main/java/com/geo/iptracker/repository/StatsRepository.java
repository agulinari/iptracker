package com.geo.iptracker.repository;

import org.springframework.data.redis.core.ZSetOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StatsRepository {

    Mono<Double> insertStat(String countryCode, String countryName, Double distance);

    Mono<String> getMaxDistance();

    Mono<String> getMinDistance();

    Flux<ZSetOperations.TypedTuple<String>> getDistances();

    Flux<ZSetOperations.TypedTuple<String>> getCalls();

}
