package com.xinyuan.opentelemetryexample.controllers;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dxinyuan
 */

@RestController
@AllArgsConstructor
public class PingController {

    private final OpenTelemetry openTelemetry;

    @GetMapping("/ping")
    public String ping() {
        try {
            internalCall();
            return "pong";
        } catch (Exception e) {
            return "exception occurred";
        }
    }

    @GetMapping("/pong")
    public String pong(@RequestParam String id) {
        try {
            // OpenTelemetry has a maximum of 128 Attributes by default for Spans, Links, and Events.
            Tracer tracer = openTelemetry.getTracer("ConfigureTraceExample");
            Span multiAttrSpan = tracer.spanBuilder("Example Span Attributes").startSpan();
            multiAttrSpan.setAttribute("Attribute 1", "first attribute value");
            multiAttrSpan.setAttribute("Attribute 2", "second attribute value");
            multiAttrSpan.end();
            internalCall();
            return "pong"+id;
        } catch (Exception e) {
            return "exception occurred";
        }
    }

    private void internalCall() throws InterruptedException {
        Thread.sleep(1);
    }

}
