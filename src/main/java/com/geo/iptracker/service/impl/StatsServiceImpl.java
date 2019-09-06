package com.geo.iptracker.service.impl;

import com.geo.iptracker.domain.dto.CountryDistance;
import com.geo.iptracker.domain.dto.IpTrackerResponse;
import com.geo.iptracker.repository.StatsRepository;
import com.geo.iptracker.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public Mono<IpTrackerResponse> collectStats(IpTrackerResponse r) {

        return statsRepository.insertStat(r.getIsoCode(), r.getCountryName(), r.getDistanceToBsAs().getKms())
                .map(b -> r);

    }

    @Override
    public Mono<CountryDistance> getCountryWithMaxDistance() {
        return statsRepository.getMaxDistance().map(tuple ->
                CountryDistance.builder().countryName(tuple.getValue()).distance(tuple.getScore()).build());
    }

    @Override
    public Mono<CountryDistance> getCountryWithMinDistance() {
        return statsRepository.getMinDistance().map(tuple ->
                CountryDistance.builder().countryName(tuple.getValue()).distance(tuple.getScore()).build());
    }

    @Override
    public Mono<Double> getAverageDistance() {
        Flux<ZSetOperations.TypedTuple<String>> fDistances = statsRepository.getDistances();
        Flux<ZSetOperations.TypedTuple<String>> fCalls = statsRepository.getCalls();
        Mono<Map<String, Double>>  mMapCalls = fCalls.collectMap(tuple -> tuple.getValue(), tuple -> tuple.getScore());
        Mono<Map<String, Double>> mMapDistances = fDistances.collectMap(tuple -> tuple.getValue(), tuple -> tuple.getScore());
        return mMapCalls.zipWith(mMapDistances, (mapCalls, mapDistances) -> calculateAverage(mapCalls, mapDistances));
    }

    private Double calculateAverage(Map<String, Double> mapCalls, Map<String, Double> mapDistances) {
        double average = 0.0;
        double n = 0.0;

        // total calls
        for (Double calls: mapCalls.values()) {
            n = n + calls;
        }

        // average
        for (Map.Entry<String, Double> entryCall : mapCalls.entrySet()) {
            Double calls = entryCall.getValue();
            Double distance = mapDistances.get(entryCall.getKey());
            average =  average + (distance.doubleValue() * calls.doubleValue()) / n;
        }
        return average;
    }

}
