package com.spring.camel.routes.c;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestConfigurationDefinition;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class RestApiConsumerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration().host("localhost").port(8000);
        from("timer:rest-api-consumer?period=10000")
                .setHeader("from", ()->"USD")
                .setHeader("to", ()->"INR")
                .to("rest:get:/currency-exchange/from/{from}/to/{to}")
                .log("${body}");
    }
}
