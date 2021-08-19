package com.miguel.application.web.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping(path = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public String test (){
        return "Hello Word";
    }
}