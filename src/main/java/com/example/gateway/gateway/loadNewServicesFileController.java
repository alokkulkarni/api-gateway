package com.example.gateway.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class loadNewServicesFileController {

    @Value("${gateway.services}")
    private String filePath;

    private ServiceDetailsProvider serviceDetailsProvider;

    public loadNewServicesFileController(ServiceDetailsProvider serviceDetailsProvider) {
        this.serviceDetailsProvider = serviceDetailsProvider;
    }

    @RequestMapping("/filePath")
    String getFileName() {
        return serviceDetailsProvider.reloadServiceYaml(filePath);
    }
}
