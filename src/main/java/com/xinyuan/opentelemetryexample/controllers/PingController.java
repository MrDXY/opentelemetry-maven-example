package com.xinyuan.opentelemetryexample.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dxinyuan
 */

@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        try {
            internalCall();
            return "pong";
        } catch (Exception e) {
            return "exception occurred";
        }
    }

    private void internalCall() throws InterruptedException {
        Thread.sleep(1);
    }

}
