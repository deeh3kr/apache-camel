package com.spring.camel.routes.patterns;

import com.spring.camel.dto.CurrencyExchange;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@Component
public class EipPatternsRouter extends RouteBuilder {
    @Autowired
    private ConvertToString convertToString;
    @Autowired
    private SplitterComponent splitterComponent;
    @Autowired
    private DynamicRouterBean dynamicRouterBean;

    public class ArrayListAggregationStrategy implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            Object newBody = newExchange.getIn().getBody();
            ArrayList<Object> list = null;
            if(oldExchange == null){
                list = new ArrayList<Object>();
                list.add(newBody);
                newExchange.getIn().setBody(list);
                return newExchange;
            }else{
                list = oldExchange.getIn().getBody(ArrayList.class);
                list.add(newBody);
                return oldExchange;
            }
        }
    }

    @Override
    public void configure() throws Exception {

        //if any message fails the processing, it will be pushed to deadLetter Queue
        errorHandler(deadLetterChannel("activemq:dead-letter-queue"));

//        from("timer:multicast?period=10000")
//                .multicast() //means we can send to multiple end points
//                .to("log:something1", "log:something2");
//

//        from("file:files/csv")
//                .unmarshal().csv()
//                .split(body())
//                .bean(convertToString)
//                .multicast()
//                .to("log:split-files", "activemq:split-queue");

//        from("file:files/csv")
//                .convertBodyTo(String.class)
//              //  .split(body(), ",") //split based on ','
//                .split(method(splitterComponent)) //split with Method
//                .multicast()
//                .to("log:split-files", "activemq:split-queue");

        //Aggregate the incoming messages into single data (we grouped based on "to" field)
//        from("file:files/aggregate-json")
//                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
//                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
//                .completionSize(3)
//                .to("log:aggregate-json");

        //Route Slip
        String routingSlip = "direct:endpoint1, direct:endpoint2";

//        from("timer:routingSlip?period=10000")
//                .transform().constant("Mhy Message is Hardcoded")
//                .routingSlip(simple(routingSlip));

        from("direct:endpoint1")
                .to("{{endpoint-for-logging}}"); //fetch from Property file

        from("direct:endpoint2")
                .to("log:directEndpoint2");

        from("direct:endpoint3")
                .to("log:directEndpoint3");

        //Dynamic Routing Patterns (Route based on some logic)
        from("timer:routingSlip?period=10000")
                .transform().constant("Mhy Message is Hardcoded")
                .dynamicRouter(method(dynamicRouterBean));


    }

}

@Component
class ConvertToString{
    public String convertIntoString(@Body String body){
        return body.toString();
    }
}

@Component
class SplitterComponent{
    public List<String> splitInput(String body){
        return List.of("ABC", "DEF");
    }
}

@Component
@Slf4j
class DynamicRouterBean{

    int invocation;

    public String decideNextEndpoint(
            @ExchangeProperties Map<String, String> properties,
            @Headers Map<String, String> headers,
            @Body String body){
        invocation++;
        log.info("Properties {} \nHeaders {}, \nBody {}", properties, headers, body);
        if(invocation%3 == 0){
            return "direct:endpoint1";
        }
        if(invocation%3 == 1){
            return "direct:endpoint2, direct:endpoint3";
        }
        else{
            return null;
        }

    }

}