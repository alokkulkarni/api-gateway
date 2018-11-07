package com.example.gateway.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.tuple.Tuple;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

@Component
public
class RewritePathGatewayFilter implements GatewayFilterFactory {

    public static final String OPCODE_KEY = "regexp";

    @Autowired
    private NettyRoutingFilter nettyRoutingFilter;

    @Override
    public
    List<String> argNames() {
        return Arrays.asList( OPCODE_KEY);
    }

    @Override
    public
    GatewayFilter apply( Tuple args ) {
        final String regex = args.getString(OPCODE_KEY);
        return apply(regex);
    }

    public GatewayFilter apply(String regex) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String opCode = headers.toSingleValueMap().get(regex);
            Optional<Service> service = null;
            try {
                service = ServiceDetailsProvider.getInstance().getServiceBasedOnOpCode( opCode );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            final URI uri;
            System.out.println("******************** " + service.get().toString());
            if (service.isPresent()) {
                uri = URI.create( service.get().getService() );
            } else {
                setResponseStatus(exchange, HttpStatus.NOT_FOUND);
                final ServerHttpResponse response = exchange.getResponse();
                return response.setComplete();
            }


            //            String token = authorizationService.getAuthorizationToken();
//            exchange.getResponse().getHeaders().add( "Authorization", token );

            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, uri);
            
            return nettyRoutingFilter.filter( exchange, chain );
        };
    }

}
