package com.spring.camel.routes.a;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
//@Component  //We commented, so that below code wont get executes
public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;
    @Autowired
    private SimpleLoggingProcessingComponent simpleLoggingProcessingComponent;

    @Override
    public void configure() throws Exception {
        //listen from queue (here we use timer)
        //transform the data
        //save into DB (here we log)

        from("timer:first-timer")

        //        .transform().constant("My Constant Message")
        //        .transform().constant("Time is: " + LocalDateTime.now())
                .bean(getCurrentTimeBean, "getCurrentTime")  //this is to transform the input body
          //      .bean(simpleLoggingProcessingComponent, "process") //this is to process the message body
                .process(new SimpleLoggingProcessor())
                .to("log:first-timer");

    }

}

@Slf4j
class SimpleLoggingProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Simple Logging Processor: {}", exchange.getMessage().getBody());
    }
}

@Component
class GetCurrentTimeBean{
    public String getCurrentTime(){
        return "Time now is: " + LocalDateTime.now();
    }
}

@Component
@Slf4j
class SimpleLoggingProcessingComponent{
    public void process(String message){
        log.info("logged from SimpleLoggingProcessingComponent: " + message);
    }
}
