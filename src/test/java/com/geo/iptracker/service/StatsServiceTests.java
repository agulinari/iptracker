package com.geo.iptracker.service;

import com.geo.iptracker.domain.dto.CountryDistance;
import com.geo.iptracker.repository.StatsRepository;
import com.geo.iptracker.service.impl.StatsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class StatsServiceTests {

    private StatsService service;

    @MockBean
    private StatsRepository statsRepository;

    @Before
    public void setUp() {
        service = new StatsServiceImpl(statsRepository);
    }

    @Test
    public void getMaxDistance() {

        // Expected
        CountryDistance expected = CountryDistance.builder().countryName("China").distance(124000.00).build();

        // Mocks setup
        ZSetOperations.TypedTuple<String> china = new DefaultTypedTuple<>("China", 124000.00);

        BDDMockito.when(statsRepository.getMaxDistance()).thenReturn(Mono.just(china));

        // Service call
        Mono<CountryDistance> res = service.getCountryWithMaxDistance();

        // Verification
        StepVerifier.create(res)
                .assertNext(r -> assertEquals(r, expected))
                .verifyComplete();
    }

    @Test
    public void getMinDistance() {
        // Expected
        CountryDistance expected = CountryDistance.builder().countryName("Argentina").distance(500.00).build();

        // Mocks setup
        ZSetOperations.TypedTuple<String> arg = new DefaultTypedTuple<>("Argentina", 500.00);

        BDDMockito.when(statsRepository.getMinDistance()).thenReturn(Mono.just(arg));

        // Service call
        Mono<CountryDistance> res = service.getCountryWithMinDistance();

        // Verification
        StepVerifier.create(res)
                .assertNext(r -> assertEquals(r, expected))
                .verifyComplete();
    }

    @Test
    public void getAverageDistanceOk() {

        // Expected response
        int expected = 5254;

        // Mocks setup
        ZSetOperations.TypedTuple<String> dis1 = new DefaultTypedTuple<>("Brasil", 2862.0);
        ZSetOperations.TypedTuple<String> dis2 = new DefaultTypedTuple<>("España", 10040.0);
        ZSetOperations.TypedTuple<String> call1 = new DefaultTypedTuple<>("España", 5.0);
        ZSetOperations.TypedTuple<String> call2 = new DefaultTypedTuple<>("Brasil", 10.0);

        Flux<ZSetOperations.TypedTuple<String>> fluxDistances = Flux.just(dis1, dis2);
        Flux<ZSetOperations.TypedTuple<String>> fluxCalls = Flux.just(call1, call2);

        BDDMockito.when(statsRepository.getDistances()).thenReturn(fluxDistances);
        BDDMockito.when(statsRepository.getCalls()).thenReturn(fluxCalls);

        // Service call
        Mono<Double> res = service.getAverageDistance();

        // Verification
        StepVerifier.create(res)
                .assertNext(r -> assertEquals(r.intValue(), expected))
                .verifyComplete();

    }


}
