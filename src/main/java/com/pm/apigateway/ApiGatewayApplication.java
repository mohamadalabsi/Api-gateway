package com.pm.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }


//#first thing change  the name of the file to application.yml
//
//    server:
//    port: 4004
//
//
//    spring:
//    cloud:
//    gateway:
//    routes: #add all the routes for patient microservice
//          - id: patient-service-route
//    uri: http://patient-service:4000 # internal address of patient service that running
//            #            inside docker container
//#            docker will resolve this name to a given container and has patient-service  name and
//#  send request to that container
//    predicates: # to determine which requests should be routed to patient service
//                -Path=/api/patients/** # all requests that start with /api/patients/ will be
// #              routed to patient service
// filters: # to rewrite the predicate before forwarding the request to patient service
// - StripPrefix=1 # remove /api from the path before forwarding the request to
// #              - patient service
// # removes the first path segment after the leading slash. With the predicate
// #  If you wanted to remove both /api/patients, you would set stripPrefix=2.
// # example Rest client call -->  http://localhost:4004/api/patients
// #rewrite the path to remove (filters ) /api/patients so form Api gateway -->
// #  http://patient-service:4000/patients
// # then it will be sent to patient service app (spring boot app) --> /patients endpoint in controller
//
// # for api-docs
// - id: api-docs-patient-route
// uri: http://patient-service:4000
// predicates:
// - path=/api-docs/patients
// filters:
// -rewritePath=/api-docs/patients,/v3/api-docs
//
// #          so what will happen is REST client --> http://localhost:4004/api-docs/patients
// # will be rewritten to by API-Gateway --> http://patient-service:4000/v3/api-docs
//
//
// # then add other routes for other microservices

}
