package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {
    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieInfo = List.of(new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"),
                        LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "Heath Ledger"),
                        LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
                        LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieInfo).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        Flux<MovieInfo> moviesInfoFlux = movieInfoRepository.findAll().log();
        StepVerifier.create(moviesInfoFlux).expectNextCount(3).verifyComplete();
    }

    @Test
    void findById() {
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.findById("abc").log();
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> assertEquals("Dark Knight Rises", movieInfo.getName())).verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        Mono<MovieInfo> savedMovieInfo = movieInfoRepository.save(
                new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"),
                        LocalDate.parse("2005-06-15"))).log();
        StepVerifier.create(savedMovieInfo).assertNext(movieInfo -> {
            assertNotNull(movieInfo.getId());
            assertEquals("Batman Begins", movieInfo.getName());
        }).verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        MovieInfo movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2006);

        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(movieInfo).log();

        StepVerifier.create(movieInfoMono).assertNext(mi -> assertEquals(2006, mi.getYear())).verifyComplete();
    }
}