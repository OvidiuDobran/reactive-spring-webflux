package com.reactivespring.router;

import com.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewsRouter(ReviewHandler reviewHandler) {
        return route().nest(path(""), builder -> builder.POST("", reviewHandler::addReview)
                                                        .GET("/v1/reviews", reviewHandler::getReviews).DELETE("/{id}",
                                                                                                              request -> reviewHandler.deleteReview(
                                                                                                                      request)))
                      .GET("/v1/helloworld", (request -> ServerResponse.ok().bodyValue("Hello World!"))).build();
    }
}
