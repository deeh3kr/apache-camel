package com.spring.camel.controller;

import com.spring.camel.routes.a.MyFirstTimerRouter;
import com.spring.camel.routes.b.MyFileRoute;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RouteController {

//    @Autowired
//    private MyFirstTimerRouter myFirstTimerRouter;
    @Autowired
    private MyFileRoute myFileRoute;

//    @GetMapping("/first-timer")
//    public ResponseEntity<?> ExecuteFirstTimerRouter() throws Exception {
//        log.info("Initiated MyFirstTimerRouter...!");
//        myFirstTimerRouter.getCamelContext().getRouteController().startRoute("First-Timer-Route");
//        return new ResponseEntity<>("Executed", HttpStatus.OK);
//    }

    @GetMapping("/my-file-route")
    public ResponseEntity<?> ExecuteMyFileRoute() throws Exception {
        log.info("Initiating MyFileRouter...!");
        myFileRoute.getCamelContext().getRouteController().startRoute("Files-Input-Route");
        return new ResponseEntity<>("Executed", HttpStatus.OK);
    }

}
