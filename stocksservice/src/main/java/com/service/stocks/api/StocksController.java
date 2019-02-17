package com.service.stocks.api;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.service.stocks.config.StocksConfiguration;
import com.service.stocks.model.Stock;
import com.service.stocks.repositories.StocksRepository;
import com.service.stocks.services.StocksService;
import com.service.stocks.services.exceptions.InvalidStockException;
import com.service.stocks.services.exceptions.StockNotFoundException;

@RestController
public class StocksController {

	private StocksRepository repository;
	private StocksService service;
	private StocksConfiguration configuration;

	public StocksController(StocksRepository repository, StocksService service, StocksConfiguration configuration) {
		this.repository = repository;
		this.service = service;
		this.configuration = configuration;
	}
	
	@PostConstruct
	public void init() {
		repository.addAll(configuration.getLoad());
	}
	
	@GetMapping("/stocks")
	public Collection<Stock> getStockList() {
		return service.getStocks();
	}
	
	@GetMapping("/stocks/{id}")
	public ResponseEntity<Stock> getStockList(@PathVariable long id) {
		try {
			return ResponseEntity.ok().body(service.get(id));
		} catch (StockNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/stocks")
	public ResponseEntity<Stock> addNewStock(@RequestBody Stock stock, UriComponentsBuilder b) {
		try {
			stock = service.add(stock);
			
		} catch (InvalidStockException e) {
			return ResponseEntity.badRequest().build();
		}
		
		UriComponents uriComponents = 
		        b.path("/stocks/{id}").buildAndExpand(stock.getId());
		
		return ResponseEntity.created(uriComponents.toUri()).body(stock);
	}
	
	@PutMapping("/stocks/{id}")
	public ResponseEntity<Stock> updateStock(@PathVariable long id, @RequestBody Stock stock, UriComponentsBuilder b) {
		
		try {
			stock = service.save(stock.copy(id));
			
		} catch (InvalidStockException e) {
			return ResponseEntity.badRequest().build();
		} catch (StockNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(stock);
	}
}
