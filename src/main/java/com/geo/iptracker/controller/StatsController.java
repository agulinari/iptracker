package com.geo.iptracker.controller;

import com.geo.iptracker.domain.dto.CountryDistance;
import com.geo.iptracker.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/stats")
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/max")
    public Mono<ResponseEntity<CountryDistance>> getCountryWithMaxDistance() {
        return this.statsService.getCountryWithMaxDistance().map(c -> ResponseEntity.ok(c));
    }

    @GetMapping("/min")
    public Mono<ResponseEntity<CountryDistance>> getCountryWithMinDistance() {
        return this.statsService.getCountryWithMinDistance().map(c -> ResponseEntity.ok(c));
    }

    @GetMapping("/average")
    public Mono<ResponseEntity<Double>> getAverageDistance() {
        return this.statsService.getAverageDistance().map(a -> ResponseEntity.ok(a));
    }
}
