package com.example.demo.services;

import com.example.demo.models.CurrencyList;
import com.example.demo.models.SingleCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CurrencyConverterServiceImplementation implements CurrencyConverterService {
    private final RestTemplate restTemplate;
    private static final String url = "https://www.cbr-xml-daily.ru/daily_json.js";
    private CurrencyList currencyList;

    @Autowired
    public CurrencyConverterServiceImplementation(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        this.restTemplate.setMessageConverters(messageConverters);
    }

    private void makeWebRequestToService(){
        currencyList = restTemplate.getForObject(url, CurrencyList.class);
    }

    @Override
    public double toDollars(double rubles) {
        if(currencyList == null){
            makeWebRequestToService();
        }
        System.out.println(currencyList);
        SingleCurrency USD = currencyList.getValute().get("USD");
        return rubles/USD.getValue();
    }

    @Override
    public double toEuro(double rubles) {
        if(currencyList == null){
            makeWebRequestToService();
        }
        SingleCurrency EUR = currencyList.getValute().get("EUR");
        return rubles/EUR.getValue();
    }
}
