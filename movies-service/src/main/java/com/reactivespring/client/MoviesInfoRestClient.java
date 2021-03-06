package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.exception.MoviesInfoClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class MoviesInfoRestClient {
    private WebClient webClient;

    @Value("${restClient.moviesInfoUrl}")
    private String movieInfoUrl;

    public MoviesInfoRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MovieInfo> retrieveMovieInfo(String movieId) {
        var url = movieInfoUrl.concat("/{id}");
        return webClient.get().uri(url, movieId).retrieve().onStatus(HttpStatus::is4xxClientError, clientResponse -> {
            if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                return Mono.error(
                        new MoviesInfoClientException("No MovieInfo found.", clientResponse.statusCode().value()));
            }
            return clientResponse.bodyToMono(String.class).flatMap(responseMessage -> Mono.error(
                    new MoviesInfoClientException(responseMessage, clientResponse.statusCode().value())));
        }).bodyToMono(MovieInfo.class).log();
    }
}
