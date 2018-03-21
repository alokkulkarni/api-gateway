package com.example.gateway.gateway;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
public
class DataImportConfiguration {

//    @Bean
//    CommandLineRunner runner() {
//        return (String ... args) -> getCities().stream().forEach(city -> System.out.println( city.getName() + " " + city.getService() ) );
//    }



//    public List<Service> getCities() {
//        Properties yaml = loadCitiesYaml();
//        MutablePropertySources propertySources = new MutablePropertySources(  );
//        propertySources.addFirst( new PropertiesPropertySource( "services", yaml ) );
//        ConfigurationPropertySource source = new MapConfigurationPropertySource(yaml);
//        return new Binder(source).bind("services", Bindable.listOf(Service.class)).get();
//    }
//
//    private
//    Properties loadCitiesYaml() {
//        YamlPropertiesFactoryBean properties = new YamlPropertiesFactoryBean();
//        properties.setResources(new ClassPathResource("services.yml"));
//        return properties.getObject();
//    }
}
