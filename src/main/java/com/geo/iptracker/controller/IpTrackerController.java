package com.geo.iptracker.controller;

import com.geo.iptracker.domain.dto.IpAddressRequest;
import com.geo.iptracker.domain.dto.IpTrackerResponse;
import com.geo.iptracker.service.IpTrackerService;
import com.geo.iptracker.service.StatsService;
import com.geo.iptracker.util.IpTrackerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class IpTrackerController {

    private final IpTrackerService ipTrackerService;
    private final StatsService statsService;

    @Autowired
    public IpTrackerController(IpTrackerService ipTrackerService, StatsService statsService) {
        this.ipTrackerService = ipTrackerService;
        this.statsService = statsService;
    }

    @PostMapping("/api/ipaddress")
    public Mono<ResponseEntity<IpTrackerResponse>> resolveIp(@RequestBody IpAddressRequest request) {

        if (!IpTrackerUtil.isValidIP(request.getIp())) {
            log.info("Ip Address {} is invalid", request.getIp());
            return Mono.just(ResponseEntity.badRequest().build());
        }
        return ipTrackerService.resolveIp(request.getIp())
                .flatMap(r -> statsService.collectStats(r))
                .map(r -> ResponseEntity.ok(r));

    }

}
