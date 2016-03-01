package com.rpicloud.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mixmox on 01/03/16.
 */

@RestController
public class ServiceController extends BaseController {

    @RequestMapping("/test")
    public ResponseEntity<List<String>> tests() throws InterruptedException {

        System.out.println("IN SERVICE CONTROLLER");
        List<String> list = new ArrayList<>();
        list.add("H");
        list.add("e");
        list.add("l");
        list.add("l");
        list.add("o");

        return new ResponseEntity<List<String>>(list, HttpStatus.OK);
    }



    // TODO: Move to a ConfigurationController with a shared ServerState
    @RequestMapping("/exceptions/{exception}")
    public String crash(@PathVariable String exception) {
        state.setException(exception);
        return "I'm gonna throw a '" + state.getException() + "' next time you call me!";
    }
}
