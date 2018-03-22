package com.example.gateway.gateway;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public
class ServiceDetailsProvider {
    private static ServiceDetailsProvider ourInstance = new ServiceDetailsProvider();

    public static
    ServiceDetailsProvider getInstance() {
        return ourInstance;
    }

    private
    ServiceDetailsProvider() {
    }

    public
    Optional<Service> getServiceBasedOnOpCode( String OpCode) {
        Optional<Service> service1 = Optional.ofNullable( getCities().stream().filter( service -> service.getName().equalsIgnoreCase( OpCode ) ).findAny().orElse( null ) );
        return service1;
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
