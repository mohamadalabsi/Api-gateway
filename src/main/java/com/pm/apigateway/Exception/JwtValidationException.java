package com.pm.apigateway.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice // tells spring that this class is going to handle exceptions
public class JwtValidationException {
//    we are getting 500 response when we pass invalid token so we have to handle that exception and return 401 instead of 500 when the token is invalid


    @ExceptionHandler(WebClientResponseException.Unauthorized.class)
    public Mono<Void> handleUnauthorizedException(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
// so we are specifing a custum exception so when our apigateway tries to call the validation
// endpoint on the auth server and it returns 401 unauthorized response instead of returning 500
// internal server error response from the apigateway we are going to intercept this and going to
// insure that the response that get send back from the apigateway is 401 unauthorized to frontend
    }

}
