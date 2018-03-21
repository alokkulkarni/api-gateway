package com.example.gateway.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.tuple.Tuple;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

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
            Optional<Service> service1 = Optional.ofNullable( getCities().stream().filter( service -> service.getName().equalsIgnoreCase( opCode ) ).findAny().orElse( null ) );
            final URI uri;

            if (service1.isPresent()) {
                uri = URI.create( service1.get().getService() );
            } else {
                setResponseStatus(exchange, HttpStatus.NOT_FOUND);
                final ServerHttpResponse response = exchange.getResponse();
                return response.setComplete();
            }
            
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, uri);
            
            return nettyRoutingFilter.filter( exchange, chain );
        };
    }


    private
    List<Service> getCities() {
        Properties yaml = loadCitiesYaml();
        MutablePropertySources propertySources = new MutablePropertySources(  );
        propertySources.addFirst( new PropertiesPropertySource( "services", yaml ) );
        ConfigurationPropertySource source = new MapConfigurationPropertySource(yaml);
        return new Binder(source).bind("services", Bindable.listOf(Service.class)).get();
    }

    private
    Properties loadCitiesYaml() {
        YamlPropertiesFactoryBean properties = new YamlPropertiesFactoryBean();
        properties.setResources(new ClassPathResource("services.yml"));
        return properties.getObject();
    }
}
