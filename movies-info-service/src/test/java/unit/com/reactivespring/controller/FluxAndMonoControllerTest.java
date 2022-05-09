package com.reactivespring.controller;

import com.reactivespring.FluxAndMonoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Objects;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux_approach1() {
        webTestClient.get().uri("/flux").exchange().expectStatus().is2xxSuccessful().expectBodyList(Integer.class)
                .hasSize(3);
    }

    @Test
    void flux_approach2() {
        var flux =
                webTestClient.get().uri("/flux").exchange().expectStatus().is2xxSuccessful().returnResult(Integer.class)
                        .getResponseBody();

        StepVerifier.create(flux).expectNext(1, 2, 3).verifyComplete();
    }

    @Test
    void flux_approach3() {
        webTestClient.get().uri("/flux").exchange().expectStatus().is2xxSuccessful().expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    var resBody = listEntityExchangeResult.getResponseBody();
                    assert Objects.requireNonNull(resBody).size() == 3;
                });
    }

    @Test
    void stream_approach2() {
        var flux =
                webTestClient.get().uri("/stream").exchange().expectStatus().is2xxSuccessful().returnResult(Long.class)
                        .getResponseBody();

        StepVerifier.create(flux).expectNext(0L, 1L, 2L, 3L).thenCancel().verify();
    }
}