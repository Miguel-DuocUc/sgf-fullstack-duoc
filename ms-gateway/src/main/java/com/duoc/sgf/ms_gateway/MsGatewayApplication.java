package com.duoc.sgf.ms_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class MsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGatewayApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> authRoute() {

		return GatewayRouterFunctions.route()
				.route(
						GatewayRequestPredicates.path("/api/v1/auth/**"),
						HandlerFunctions.http()
				)
				.before(BeforeFilterFunctions.uri("http://localhost:8081"))

				.route(
						GatewayRequestPredicates.path("/api/v1/logistics/**"),
						HandlerFunctions.http()
				)
				.before(BeforeFilterFunctions.uri("http://localhost:8082"))

				.route(
						GatewayRequestPredicates.path("/api/v1/alerts/**"),
						HandlerFunctions.http()
				)
				.before(BeforeFilterFunctions.uri("http://localhost:8083"))

				.route(
						GatewayRequestPredicates.path("/api/v1/audit/**"),
						HandlerFunctions.http()
				)
				.before(BeforeFilterFunctions.uri("http://localhost:8084"))

				.build();
	}
}