package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInfoControllerTest {

    static final String MOVIES_INFO_URL = "/v1/movieinfos";
    @Autowired
    MovieInfoRepository movieInfoRepository;
    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var movieInfo = List.of(new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"),
                        LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "Heath Ledger"),
                        LocalDate.parse("2008-07-18")));

        movieInfoRepository.saveAll(movieInfo).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void addMovieInfo() {
        var body = new MovieInfo("abc", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
                LocalDate.parse("2012-07-20"));
        webTestClient.post().uri(MOVIES_INFO_URL).bodyValue(body).exchange().expectStatus().isCreated()
                .expectBody(MovieInfo.class).consumeWith(res -> {
                    var resBody = res.getResponseBody();
                    assert resBody != null;
                    assert resBody.getId() != null;
                });
    }

    @Test
    void getAllMovieInfos() {
        webTestClient.get().uri(MOVIES_INFO_URL).exchange().expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class).hasSize(3);
    }

//    @Test
//    void getMovieInfoById() {
//        var movieInfoId = "abc";
//        webTestClient.get().uri(MOVIES_INFO_URL + "/{id}", movieInfoId).exchange().expectStatus().is2xxSuccessful()
//                .expectBody(MovieInfo.class).consumeWith(res -> {
//                    assert res.getResponseBody() != null;
//                });
//    }
}