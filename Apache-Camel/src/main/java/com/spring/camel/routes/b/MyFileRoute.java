package com.spring.camel.routes.b;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Header;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class MyFileRoute extends RouteBuilder {

    @Autowired
    private DeciderBean deciderBean;

    @Override
    public void configure() throws Exception {

       // getCamelContext().setAutoStartup(false);

        from("file:files/input")
                .routeId("Files-Input-Route")
                .transform().body(String.class)
                .choice() //Content based Routing
                .when(simple("${file:ext} ends with 'xml'"))
                    .log("XML File")
                //.when(simple("${body} contains 'USD'"))
                .when(method(deciderBean))
                    .log("JSON File Contains USD")
                .otherwise()
                    .log("Not JSON or XML File")
                .end()
                .log("${body}")
                .to("direct://log-file-values");
            //    .to("file:files/output");

        //Below is a reusable Route
        from("direct:log-file-values")
                .log(" \nMessageHistory: ${messageHistory} \nAbsolutePath: ${file:absolute.path}")
                .log(" \nFileName: ${file:name} \nFileExtension: ${file:name.ext} \nName.Noext: ${file:name.noext} \nOnlyName: ${file:onlyname}")
                .log(" \nParent: ${file:parent} \nFilePath: ${file:path} \nFileAbsolute: ${file:absolute}")
                .log(" \nFileSize: ${file:size} \nFileModified: ${file:modified}")
                .log(" \nRouteId: ${routeId} \nCamelId: ${camelId} \nBody: ${body}");
    }
}

@Component
@Slf4j
//for complex logic to decide the condition
class DeciderBean{
    public boolean isThisConditionMet(@Body String body, @Headers Map<String, String> headers,
                                      @ExchangeProperties Map<String, String> exchangeProperties){
        log.info("Body: {} \nHeaders {} \nExchangeProperties {}", body, headers, exchangeProperties );

        return true;
    }
}
