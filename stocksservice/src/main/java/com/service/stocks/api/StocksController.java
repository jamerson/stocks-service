package com.service.stocks.api;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.service.stocks.config.StocksConfiguration;
import com.service.stocks.model.Stock;
import com.service.stocks.services.StocksService;
import com.service.stocks.services.exceptions.InvalidStockPriceException;
import com.service.stocks.services.exceptions.StockNotFoundException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ExampleProperty;
import io.swagger.annotations.Example;

@RestController
@RequestMapping("/stocks")
@Api("/stocks")
public class StocksController {
    private StocksService service;
    private StocksConfiguration configuration;

    public StocksController(StocksService service, StocksConfiguration configuration) {
        this.service = service;
        this.configuration = configuration;
    }

    @PostConstruct
    public void init() {
        service.addAll(configuration.getLoad());
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "View a list of stocks", response = Stock[].class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = Stock[].class)
    })
    public Collection<Stock> getStockList() {
        return service.getStocks();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "View an specific stock", response = Stock.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = Stock.class),
        @ApiResponse(code = 404, message = "Stock not found")
    })
    public ResponseEntity<Stock> getStock(@PathVariable long id) {
        try {
            return ResponseEntity.ok().body(service.get(id));
        } catch (StockNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create a new stock", response = Stock.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Stock created", response = Stock.class),
        @ApiResponse(code = 400, message = "Inconsistent input data")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "new stock", value = "{ \"currentPrice\": 10.0, \"name\": \"New Stock\" }", required = true, paramType = "body", examples = @Example(value = {@ExampleProperty(mediaType = "application/json" ,value = "{ \"currentPrice\": 10.0, \"name\": \"New Stock\" }")}))
    })
    public ResponseEntity<Stock> addNewStock(@ApiParam(name = "new stock", value = "{ \"currentPrice\": 10.0, \"name\": \"New Stock\" }") @RequestBody Stock stock, UriComponentsBuilder b) {
        try {
            stock = service.add(stock);

        } catch (InvalidStockPriceException e) {
            return ResponseEntity.badRequest().build();
        }

        UriComponents uriComponents = b.path("/stocks/{id}").buildAndExpand(stock.getId());

        return ResponseEntity.created(uriComponents.toUri()).body(stock);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update the price of an existing stock", response = Stock.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success", response = Stock.class),
        @ApiResponse(code = 400, message = "Incosistent input data"),
        @ApiResponse(code = 404, message = "Stock not found")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "new price", value = "{ \"currentPrice\": 88.0 }", required = true, paramType = "body", examples = @Example(value = {@ExampleProperty(mediaType = "application/json" ,value = "{ \"currentPrice\": 88.0 }")}))
    })
    public ResponseEntity<Stock> updateStock(@PathVariable long id, @ApiParam(name = "new price", value = "{ \"currentPrice\": 88.0 }") @RequestBody Stock stock, UriComponentsBuilder b) {

        stock.setId(id);
        try {
            stock = service.updatePrice(id, stock.getCurrentPrice());

        } catch (InvalidStockPriceException e) {
            return ResponseEntity.badRequest().build();
        } catch (StockNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(stock);
    }
}
