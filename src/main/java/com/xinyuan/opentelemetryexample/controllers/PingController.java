package com.xinyuan.opentelemetryexample.controllers;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
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
            internalCall();
            multiAttrSpan.end();
            return "pong"+id;
        } catch (Exception e) {
            return "exception occurred";
        }
    }

    @GetMapping("/test")
    public String test() throws InterruptedException {
        Tracer tracer = openTelemetry.getTracer("com.xinyuan.opentelemetryexample.controllers");
        Meter meter = openTelemetry.getMeter("com.xinyuan.opentelemetryexample.controllers");
        LongCounter counter = meter.counterBuilder("example.counter").build();
        LongHistogram histogram = meter.histogramBuilder("super.timer").ofLongs().setUnit("ms").build();

        long startTime = System.currentTimeMillis();
        Span exampleSpan = tracer.spanBuilder("exampleSpan").startSpan();
        Context exampleContext = Context.current().with(exampleSpan);
        try (Scope scope = exampleContext.makeCurrent()) {
            counter.add(1);
            exampleSpan.setAttribute("good", true);
            exampleSpan.setAttribute("exampleNumber", 1);
            Thread.sleep(1000);
        } finally {
            histogram.record(
                    System.currentTimeMillis() - startTime, Attributes.empty(), exampleContext);
            exampleSpan.end();
        }

        return "end";
    }

    private void internalCall() throws InterruptedException {
        Thread.sleep(1);
    }

}
