package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {
    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Flux<String> namesFluxMap() {
        return namesFlux().map(String::toUpperCase).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("alex");
    }

    public Flux<String> namesFluxFilter(int stringLength) {
        return namesFlux().filter(s -> s.length() > stringLength).log();
    }

    public Flux<String> namesFluxFlatMap() {
        return namesFlux().map(String::toUpperCase).flatMap(s -> splitString(s)).log();
    }

    public Flux<String> splitString(String name) {
        return Flux.fromArray(name.split(""));
    }

    public Flux<String> namesFluxDefault(int stringLength) {
        return namesFluxFilter(stringLength).defaultIfEmpty("Empty");
    }

    public static void main(String[] args) {
        var fmService = new FluxAndMonoGeneratorService();
        fmService.namesFlux().subscribe(System.out::println);
        fmService.nameMono().subscribe(System.out::println);
    }
}
