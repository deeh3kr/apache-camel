package com.apache.camel.processor;

import com.apache.camel.dto.CurrencyExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyCurrencyExchangeProcessor {
    public void process(CurrencyExchange currencyExchange){
        log.info("Do some processing with value {}", currencyExchange.getConversionMultiple());
    }
}
