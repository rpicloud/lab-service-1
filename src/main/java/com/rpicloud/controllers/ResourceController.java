package com.rpicloud.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rpicloud.interfaces.IService2;
import com.rpicloud.models.Resource;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mixmox on 18/04/16.
 */
@Component
@RestController
public class ResourceController {


    private String service2host;
    private String service2port;

    @HystrixCommand(fallbackMethod = "open")
    @RequestMapping(value = "/resources")
    public ResponseEntity<List<Resource>> resources() {


        IService2 service2 = Feign.builder()
                .decoder(new JacksonDecoder())
                .target(IService2.class, "http://"+service2host+":"+service2port);

        // Fetch and print a list of the contributors to this library.
        List<Resource> resources = service2.resources();

        return new ResponseEntity<List<Resource>>(resources, HttpStatus.OK);

    }

    public ResponseEntity<List<Resource>> open(){
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource("Dummy data from service 1"));
        resources.add(new Resource("Dummy data from service 1"));
        resources.add(new Resource("Dummy data from service 1"));
        resources.add(new Resource("Dummy data from service 1"));
        resources.add(new Resource("Dummy data from service 1"));

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }


    @Autowired
    void setEnvironment(Environment env){
        service2host = env.getProperty("configuration.service2.host");
        service2port = env.getProperty("configuration.service2.port");
    }

    // From config?
    @ConfigurationProperties(prefix = "hystrix", ignoreUnknownFields = true)
    class HystrixProperties {
        String enabled = "false";
    }

}
