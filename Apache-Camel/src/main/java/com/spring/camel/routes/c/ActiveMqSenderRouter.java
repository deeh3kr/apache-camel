package com.spring.camel.routes.c;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        //timer
        from("timer:active-mq-timer?period=10000")  //every 10 sec
                .transform().constant("MyMessage for Active MQ")
                .log("${body}")
                .to("activemq:my-activemq-queue");
        //queue

    }
}
