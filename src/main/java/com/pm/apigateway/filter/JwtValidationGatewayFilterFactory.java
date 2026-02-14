package com.pm.apigateway.filter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

//after integration auth service login and validation with api gateway , the next step is we
// want to intercept all request to patient service ect. and we want to call the validate
// endpoint on the microservice from api gateway to check that the token that the user is sending
// along with the request to get the patients ect. is valid or not and then depending on the
// validate request we will either return unauthorized request or allow the request to continue

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
// this is a filter class that allows us to intercept http requests , apply custom logic and then
// decide whether to continue processing the request or cancel the request

    private final WebClient webClient; // we will use to make http request to auth service

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder , @Value("${auth.service.url}") String authServiceUrl) {
//  !      second parameter , we are going to take the base url of the auth service form env vars
//  in docker compose file
//        and assign it to the  authServiceUrl and we use ${auth.service.url} because it will
//        change when we use aws so in our local docker setup it is going to be auth-service:4005
//        but in aws it is : ecs.aws.nejcejjece:5000 or something like that



        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
//        we initialize the web client using the base url that was passed in

// when our app starts spring will create a new filter and will initialize the web client using
// the base url that we passed in as env var

    }
    @Override
    public GatewayFilter apply(Object config) {
//        exchange is the object that it will be passed to us by spring gateway and hold all the
//        properties of the current request
//        the chain variable is var that maneges the chain of filters that currently exist in the
//        filter chain
//        we can have as many filter as we want
//         both from AbstractGatewayFilterFactory we extended
        return (exchange, chain) ->  {
//            check the request for authorization token

//            getting the authorization header from the request and going to assign it to the token variable
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token == null || !token.startsWith("Bearer ")) {
//                simple checks on the token if it exists and if it starts with Bearer before
//                start and doing stuff with it , and if so it will return unauthorized response
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();

            }
//           if everything is okay then  call the validate endpoint on the auth service to do the
//           validation steps
            return webClient.get()
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toBodilessEntity()
                    .then(chain.filter(exchange));
// we are using the webclient and going to make a get request to the validate endpoint and

// now we have to specify which routes we want to apply this filter to and we will do that in the
// application yml file
        };

    }
}
