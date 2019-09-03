package com.geo.iptracker.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public class StatsRepositoryImpl implements StatsRepository {

    private static final String DISTANCES_KEY = "distances";
    private static final String CALLS_KEY = "calls";
    private final ReactiveRedisTemplate<String, String> redisTemplate;


    @Autowired
    public StatsRepositoryImpl(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Mono<Double> insertStat(String countryCode, String countryName, Double distance) {

        return redisTemplate.opsForZSet().add(DISTANCES_KEY, countryName, distance).flatMap(res ->
                redisTemplate.opsForZSet().incrementScore(CALLS_KEY, countryName, 1.0)
        );

    }

    @Override
    public Mono<String> getMinDistance() {
        return redisTemplate.opsForZSet()
                .rangeByScore(DISTANCES_KEY, Range.unbounded(), new RedisZSetCommands.Limit().count(1).offset(0))
                .next();
    }

    @Override
    public Mono<String> getMaxDistance() {
        return redisTemplate.opsForZSet()
                .reverseRangeByScore(DISTANCES_KEY, Range.unbounded(), new RedisZSetCommands.Limit().count(1).offset(0))
                .next();
    }

    @Override
    public Flux<ZSetOperations.TypedTuple<String>> getDistances() {
        return redisTemplate.opsForZSet().rangeWithScores(DISTANCES_KEY, Range.unbounded());
    }

    @Override
    public Flux<ZSetOperations.TypedTuple<String>> getCalls() {
        return redisTemplate.opsForZSet().rangeWithScores(CALLS_KEY, Range.unbounded());
    }

}
