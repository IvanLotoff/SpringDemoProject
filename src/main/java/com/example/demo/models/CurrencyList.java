package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class CurrencyList {
    @JsonProperty("Date")
    String date;
    @JsonProperty("PreviousDate")
    String previousDate;
    @JsonProperty("PreviousURL")
    String previousURL;
    @JsonProperty("Timestamp")
    String timestamp;
    @JsonProperty("Valute")
    Map<String, SingleCurrency> valute;
}
