package com.geo.iptracker.service;

import com.geo.iptracker.domain.dto.IpTrackerResponse;
import reactor.core.publisher.Mono;

public interface IpTrackerService {

    Mono<IpTrackerResponse> resolveIp(String ip);

}
