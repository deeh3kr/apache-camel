package com.apache.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class KafkaReceiverRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("kafka:my-kafkaTopic")
                .log("${body}")
                .to("log:received message from Kafka Topic");
    }
}
