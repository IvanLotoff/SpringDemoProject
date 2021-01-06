package com.example.demo.repositories;

import com.example.demo.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClientsRepository extends MongoRepository<Client,String> {
}
