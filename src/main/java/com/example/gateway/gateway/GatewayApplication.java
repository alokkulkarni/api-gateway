package com.example.gateway.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.net.URI;

@SpringBootApplication
public class GatewayApplication {


	@Bean
	public RouteLocator gatewayRoutes( RouteLocatorBuilder builder, ValidationGatewayFilter validationGatewayFilter, RewritePathGatewayFilter rewritePathGatewayFilter) {
		return builder
				.routes()
					.route( "customer", r -> r.path( "/customer" )
						.filters( gatewayFilterSpec -> gatewayFilterSpec.addRequestHeader( "X-gateway-Test", "new-Test" ) )
						.uri( "http://localhost:8081/client/" ))
					.route( "customerID", r -> r.path( "/customer/**")
						.filters( gatewayFilterSpec -> gatewayFilterSpec.addResponseHeader( "x-gateway-test","response-header-test" ).rewritePath( "/customer/(?<CID>.*)","/client/${CID}" ))
						.uri( "http://localhost:8081" ))
					.route( "customer2",r -> r.path( "/cust" )
						.filters( gatewayFilterSpec -> gatewayFilterSpec.redirect( "302", "https://start.spring.io" ))
						.uri( "http://localhost:8081" ))
					.route( "customer3", r -> r.path("/custHystrix")
						.filters( gatewayFilterSpec -> gatewayFilterSpec.hystrix( "cb", URI.create( "http://localhost:8081/client"  )))
						.uri( "http://localhost:8081/client/" ))
					.route( "customer4", predicateSpec -> predicateSpec.path( "/custValidate" )
							.and()
							.header( "OpCode" )
						.filters( ( GatewayFilterSpec gatewayFilterSpec ) -> gatewayFilterSpec.filter(validationGatewayFilter.apply("OpCode") )
								.filter(rewritePathGatewayFilter.apply( "OpCode" ))  )
							.uri( "http://localhost:8083" ))
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
