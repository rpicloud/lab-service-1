package com.rpicloud.controllers;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rpicloud.interfaces.IService2;
import com.rpicloud.models.Resource;
import com.rpicloud.models.ServerState;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mixmox on 18/04/16.
 */
@Component
@RestController
public class ResourceController {

    private String service2host;
    private String service2port;

    @Autowired
    void setEnvironment(Environment env){
        service2host = env.getProperty("configuration.service2.host");
        service2port = env.getProperty("configuration.service2.port");
    }
    private ServerState state = new ServerState();


    @RequestMapping(value = "/resources_nocb")
    public ResponseEntity<List<Resource>> resources_nocb() {

        IService2 service2 = Feign.builder()
                .decoder(new JacksonDecoder())
                .target(IService2.class, "http://"+service2host+":"+service2port);

        List<Resource> resources = service2.resources();

        return new ResponseEntity<List<Resource>>(resources, HttpStatus.OK);
    }

    @HystrixCommand(fallbackMethod = "open", commandKey = "resources")
    @RequestMapping(value = "/resources")
    public ResponseEntity<List<Resource>> resources() {

        IService2 service2 = Feign.builder()
                .decoder(new JacksonDecoder())
                .target(IService2.class, "http://"+service2host+":"+service2port);

        List<Resource> resources = service2.resources();

        return new ResponseEntity<List<Resource>>(resources, HttpStatus.OK);
    }

    public ResponseEntity<List<Resource>> open(){
        return new ResponseEntity<>(state.getResources().subList(0, state.getAmount()), HttpStatus.PARTIAL_CONTENT);
    }

    // Enable/disable circuit breaker
    @RequestMapping("/circuitbreaker/enabled/{enabled}")
    public ResponseEntity<String> circuitbreaker(@PathVariable boolean enabled) {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.resources.circuitBreaker.enabled", enabled);
        return new ResponseEntity<String>(enabled?"Enabled":"Disabled", HttpStatus.OK);
    }

    // Set timeout on circuit breaker
    @RequestMapping("/circuitbreaker/timeout/{duration}")
    public ResponseEntity<String> setTimeout(@PathVariable int duration){
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.resources.execution.isolation.thread.timeoutInMilliseconds", duration);
        return new ResponseEntity<String>("Duration in msec: " + duration, HttpStatus.OK);
    }

    // Enable/disable fallback
    @RequestMapping("/fallbackenabled/{enabled}")
    public ResponseEntity<String> fallback(@PathVariable boolean enabled) {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.resources.fallback.enabled", enabled);
        return new ResponseEntity<String>(enabled?"Enabled":"Disabled", HttpStatus.OK);
    }


    // Managing delay, delay and limit
    @ModelAttribute
    public void inject() throws Exception {
        state.invoke();
    }

    @RequestMapping("/delay/{interval}")
    public String delay(@PathVariable int interval) {
        state.setDelay(interval);
        return "I will sleep for " + state.getDelay() + "ms on all requests!";
    }

    @RequestMapping("/resultset/{amount}")
    public String resultSet(@PathVariable int amount) {
        state.setAmount(amount);
        return "Amount: " + state.getAmount();
    }

//    @RequestMapping("/exceptions/{exception}")
//    public String crash(@PathVariable String exception) {
//        state.setException(exception);
//        return "I'm gonna throw a '" + state.getException() + "' next time you call me!";
//    }

}
