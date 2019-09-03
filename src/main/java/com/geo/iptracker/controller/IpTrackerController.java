package com.geo.iptracker.controller;

import com.geo.iptracker.domain.dto.IpTrackerResponse;
import com.geo.iptracker.service.IpTrackerService;
import com.geo.iptracker.service.StatsService;
import com.geo.iptracker.util.GeoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class IpTrackerController {

    private final IpTrackerService ipTrackerService;
    private final StatsService statsService;

    @Autowired
    public IpTrackerController(IpTrackerService ipTrackerService, StatsService statsService) {
        this.ipTrackerService = ipTrackerService;
        this.statsService = statsService;
    }

    @GetMapping("/resolveip/{ip}")
    public Mono<ResponseEntity<IpTrackerResponse>> resolveIp(@PathVariable String ip) {

        if (!GeoUtil.isValidIP(ip)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        return ipTrackerService.resolveIp(ip)
                .flatMap(r -> statsService.collectStats(r))
                .map(r -> ResponseEntity.ok(r));

    }

    @GetMapping("/stats/max")
    public Mono<ResponseEntity<String>> getCountryWithMaxDistance() {
        return this.statsService.getCountryWithMaxDistance().map(c -> ResponseEntity.ok(c));
    }

    @GetMapping("/stats/min")
    public Mono<ResponseEntity<String>> getCountryWithMinDistance() {
        return this.statsService.getCountryWithMinDistance().map(c -> ResponseEntity.ok(c));
    }

    @GetMapping("/stats/average")
    public Mono<ResponseEntity<Double>> getAverageDistance() {
        return this.statsService.getAverageDistance().map(a -> ResponseEntity.ok(a));
    }
}
