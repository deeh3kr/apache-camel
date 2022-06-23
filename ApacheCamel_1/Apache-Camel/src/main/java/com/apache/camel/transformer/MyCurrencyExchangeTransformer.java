package com.apache.camel.transformer;

import com.apache.camel.dto.CurrencyExchange;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyCurrencyExchangeTransformer {
    public CurrencyExchange doubleConversion(CurrencyExchange currencyExchange){
        currencyExchange.setConversionMultiple(
                currencyExchange.getConversionMultiple().multiply(BigDecimal.valueOf(2)));
        return currencyExchange;
    }
}
