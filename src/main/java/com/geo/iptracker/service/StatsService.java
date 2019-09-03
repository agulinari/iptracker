package com.geo.iptracker.service;

import com.geo.iptracker.domain.dto.IpTrackerResponse;
import reactor.core.publisher.Mono;

public interface StatsService {

    Mono<IpTrackerResponse> collectStats(IpTrackerResponse r);

    Mono<String> getCountryWithMaxDistance();

    Mono<String> getCountryWithMinDistance();

    Mono<Double> getAverageDistance();

}
