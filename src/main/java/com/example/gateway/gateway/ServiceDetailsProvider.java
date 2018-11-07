package com.example.gateway.gateway;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public
class ServiceDetailsProvider {
    private static ServiceDetailsProvider ourInstance = new ServiceDetailsProvider();

    private String filePath;

    public static
    ServiceDetailsProvider getInstance() {
        return ourInstance;
    }

    private
    ServiceDetailsProvider() {
    }

    public String reloadServiceYaml(String filePath) {
        this.filePath = filePath;
        return this.filePath;
    }

    public
    Optional<Service> getServiceBasedOnOpCode( String OpCode) throws MalformedURLException {
        return Optional.ofNullable( getServices().stream().filter(service -> service.getName().equalsIgnoreCase( OpCode ) ).findAny().orElse( null ) );
    }

    private
    List<Service> getServices() throws MalformedURLException {

        Properties yaml = loadServicesYaml();
        ConfigurationPropertySource source = new MapConfigurationPropertySource(yaml);
        return new Binder( source ).bind("services", Bindable.listOf(Service.class)).get();
    }

    private
    Properties loadServicesYaml() throws MalformedURLException {
        YamlPropertiesFactoryBean properties = new YamlPropertiesFactoryBean();
        if (this.filePath.isEmpty()) {
            properties.setResources(new ClassPathResource("services.yml"));
        } else {
            properties.setResources(new FileUrlResource(this.filePath));
        }
        return properties.getObject();
    }

}
