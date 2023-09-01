#!/bin/sh
# shellcheck disable=SC2164

## initialize docker (jaeger, zipkin, prometheus, otel-collector, )
cd docker
docker-compose up -d
cd ../

## build application
mvn clean package
cp target/open-telemetry-example-0.0.1-SNAPSHOT.jar /tmp/app.jar
cp target/classes/agent/opentelemetry-javaagent.jar /tmp/opentelemetry-javaagent.jar

## start application
java -jar -javaagent:/tmp/opentelemetry-javaagent.jar /tmp/app.jar

