package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
                //.expectNext("alex", "ben", "chloe")
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        var namesFluxMap = fluxAndMonoGeneratorService.namesFluxMap();
        StepVerifier.create(namesFluxMap).expectNext("ALEX", "BEN", "CHLOE").verifyComplete();
    }

    @Test
    void namesFluxFilter() {
        var namesFluxFilter = fluxAndMonoGeneratorService.namesFluxFilter(3);
        StepVerifier.create(namesFluxFilter).expectNext("alex", "chloe").verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFluxFlatMap();
        StepVerifier.create(namesFluxFlatMap).expectNext("A", "L", "E", "X", "B", "E", "N", "C", "H", "L", "O", "E").verifyComplete();
    }

    @Test
    void namesFluxFlatMapWithDefault() {
        var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFluxFlatMap();
        StepVerifier.create(namesFluxFlatMap)
                .expectNext("A", "L", "E", "X", "B", "E", "N", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxDefault() {
        var namesFluxDefault = fluxAndMonoGeneratorService.namesFluxDefault(6);
        StepVerifier.create(namesFluxDefault).expectNext("Empty").verifyComplete();
    }
}