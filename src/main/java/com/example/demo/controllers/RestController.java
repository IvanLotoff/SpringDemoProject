package com.example.demo.controllers;

import com.example.demo.exceptions.ClientNotFoundException;
import com.example.demo.exceptions.ClientsDatabaseEmptyException;
import com.example.demo.exceptions.IncorrectClientsIdException;
import com.example.demo.exceptions.UserAlreadyExists;
import com.example.demo.models.Client;
import com.example.demo.models.Profit;
import com.example.demo.repositories.ClientsRepository;
import com.example.demo.services.CurrencyConverterService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private static final Logger log = LoggerFactory.getLogger(RestController.class);
    private final ClientsRepository repository;
    private final CurrencyConverterService converter;

    @Autowired
    public RestController(ClientsRepository repository, CurrencyConverterService converter) {
        this.repository = repository;
        this.converter = converter;
    }

    /**
     * Пост запрос для сохранения клиента в базе данных
     *
     * @param client клиент, которого нужно сохранить
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client successfully saved"),
            @ApiResponse(code = 400, message = "The user already exists")
    })
    @PostMapping("/saveClient")
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        /**
         * Проверим наличия этого элемента в монго дб, мы не знаем его ID, поэтому придется
         * искать обходные пути
         */
        if (repository.exists(Example.of(client)))
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user already exists"); <--- тоже самое по смыслу, что ниже
            throw new UserAlreadyExists(String.format("The user: %s already exists", client));
        client = repository.insert(client);
        log.info("Saved client = " + client);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    /**
     * Get запрос для получения всех клиентов из базы данных
     *
     * @return
     */
    @GetMapping("/allClients")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data successfully fetched!"),
            @ApiResponse(code = 400, message = "Clients Database is empty")
    })
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = repository.findAll();
        if (clients.isEmpty())
            throw new ClientsDatabaseEmptyException("Clients Database is empty");
        log.info("get all clients");
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    /**
     * Получаем клиента из базы данных по его ID
     *
     * @param id - String ID клиента
     * @return
     */
    @GetMapping("/findById/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client is successfully found"),
            @ApiResponse(code = 400, message = "Incorrect client's ID")
    })
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        Client possibleClient = repository.findById(id)
                .orElseThrow(() -> new IncorrectClientsIdException("Incorrect client's ID " + id));
        log.info("get client by id");
        return ResponseEntity.status(HttpStatus.OK).body(possibleClient);
    }

    /**
     * Ищем клиента по любому из параметров или их кобинации
     * Отмечу, что параметры могут быть null
     *
     * @param name               имя клиента, которого мы ищем
     * @param address            адресс клиента, которого мы ищем
     * @param isVIPclient        ВИП статус клиента, которого мы ищем
     * @param purchasesMadeSoFar количество покупок, сделанных клиентом
     * @return
     */
    @GetMapping("/find")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Clients successfully found!"),
            @ApiResponse(code = 400, message = "Client with given parameters not found")
    })
    public ResponseEntity<List<Client>> getClientByParameter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Boolean isVIPclient,
            @RequestParam(required = false) Integer purchasesMadeSoFar) {
        Client example = Client.builder()
                .name(name)
                .address(address)
                .isVIPclient(isVIPclient)
                .purchasesMadeSoFar(purchasesMadeSoFar)
                .build();
        List<Client> clients = repository.findAll(Example.of(example));
        if (clients.isEmpty())
            throw new ClientNotFoundException("Entity with parameters " + example + " not found");
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Profit was successfully calculated and converted into dollars and euros"),
    })
    @GetMapping("/getProfit")
    public ResponseEntity<Profit> getProfit() {
        List<Client> clients = repository.findAll();
        double profitEstimate = clients.stream()
                .mapToDouble(client -> client.getIsVIPclient()
                        ? 2000 * client.getPurchasesMadeSoFar()
                        : 1000 * client.getPurchasesMadeSoFar())
                .sum();
        Profit profit = Profit.builder()
                .inRubles(profitEstimate)
                .inDollars(converter.toDollars(profitEstimate))
                .inEuros(converter.toEuro(profitEstimate))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(profit);
    }
}
