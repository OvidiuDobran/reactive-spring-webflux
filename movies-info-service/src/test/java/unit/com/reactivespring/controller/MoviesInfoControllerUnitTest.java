package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerUnitTest {

    static final String MOVIES_INFOS_URL = "/v1/movieinfos";


    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private MovieInfoService movieInfoServiceMock;

    @Test
    void getAllMoviesInfo() {

        var movies = List.of(new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"),
                        LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "Heath Ledger"),
                        LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
                        LocalDate.parse("2012-07-20")));

        when(movieInfoServiceMock.getAllMovieInfos()).thenReturn(Flux.fromIterable(movies));

        webTestClient.get().uri(MOVIES_INFOS_URL).exchange().expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class).hasSize(3);
    }

    @Test
    void getMovieInfoById() {
        var expectedMovieInfo = new MovieInfo("abc", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
                LocalDate.parse("2012-07-20"));

        when(movieInfoServiceMock.getMovieInfoById(isA(String.class))).thenReturn(Mono.just(expectedMovieInfo));

        webTestClient.get().uri(MOVIES_INFOS_URL + "/{id}", "abc").exchange().expectStatus().is2xxSuccessful()
                .expectBody(MovieInfo.class).consumeWith(res -> assertNotNull(res.getResponseBody()));
    }


}
