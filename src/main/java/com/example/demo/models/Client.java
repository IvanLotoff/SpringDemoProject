package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("Clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    private String name;
    private String address;
    private Boolean isVIPclient;
    private Integer purchasesMadeSoFar;
}
