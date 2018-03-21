package com.example.gateway.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.tuple.Tuple;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

@Component
public
class ValidationGatewayFilter implements GatewayFilterFactory {

    @Override
    public
    GatewayFilter apply( Tuple args ) {
        String commandName = args.getString(NAME_KEY);
        return apply(commandName);
    }

    public GatewayFilter apply(String commandName) {
        return ( exchange , chain ) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String opCode = headers.toSingleValueMap().get(commandName);
            boolean matches = opCode.matches( "^[o-pO-P0-9]+$" );
            if (matches) {
                return chain.filter( exchange ).then( Mono.justOrEmpty( matches ).then() );
            }

            setResponseStatus(exchange, HttpStatus.BAD_REQUEST);
            final ServerHttpResponse response = exchange.getResponse();
            return response.setComplete();
        }; 
    }

    @Override
    public List<String> argNames() {
        return Arrays.asList(NAME_KEY);
    }
}
