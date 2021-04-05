package com.jakeporter.DocumentSummarizer.controllers;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
This class was added since AWS's new 'enhanced health monitoring' seems to send a bunch of requests
to the root URL aiming to get a status of 200 returned. Application 'health' will degrade otherwise.
*/

@RestController
public class RootController {

    @RequestMapping("/")
    public int returnOk() {
        return HttpStatus.SC_OK;
    }
}
