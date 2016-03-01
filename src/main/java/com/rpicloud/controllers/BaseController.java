package com.rpicloud.controllers;

import com.rpicloud.models.ServerState;
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
public class BaseController {

    protected ServerState state = new ServerState();

    // inject is called before every request on controllers inheriting from BaseController
    @ModelAttribute
    public void inject() throws Exception {
        state.invoke();
    }
}
