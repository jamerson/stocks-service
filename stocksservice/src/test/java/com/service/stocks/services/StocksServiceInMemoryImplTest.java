package com.service.stocks.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.service.stocks.config.StocksConfiguration;
import com.service.stocks.model.Stock;
import com.service.stocks.repositories.StocksRepository;
import com.service.stocks.repositories.StocksRepositoryConcurrentHashmapImpl;
import com.service.stocks.services.exceptions.StockNotFoundException;
import com.service.stocks.services.exceptions.InvalidStockException;;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StocksServiceInMemoryImplTest {
	
	private StocksService service;
	
	@Autowired
	private StocksConfiguration configuration;
	
	@Before
	public void setup() {
		StocksRepositoryConcurrentHashmapImpl repository = new StocksRepositoryConcurrentHashmapImpl();
		repository.addAll(configuration.getLoad());
		service = new StocksServiceInMemoryImpl(repository);
	}
	
	@Test
	public void getAllStocks() {
		Collection<Stock> stocks = service.getStocks();
		assertEquals(3, stocks.size());
	}
	
	@Test
	public void addNewStock() throws InvalidStockException {
		Stock stock = new Stock(0, "Stock Test", 10.0, 0);
		stock = service.add(stock);
		
		assertNotEquals(0, stock.getId());
		assertNotEquals(0, stock.getLastUpdate());
	}
	
	@Test(expected=InvalidStockException.class)
	public void addNullValue() throws InvalidStockException {
		service.add(null);
	}
	
	@Test(expected=InvalidStockException.class)
	public void addStockWithNullName() throws InvalidStockException {
		Stock stock = new Stock(0, null, 10.0, 0);
		stock = service.add(stock);
	}
	
	@Test(expected=InvalidStockException.class)
	public void addStockWithNegavitePrice() throws InvalidStockException {
		Stock stock = new Stock(0, null, -10.0, 0);
		stock = service.add(stock);
	}
	
	@Test
	public void getSavedStock() throws StockNotFoundException, InvalidStockException {
		Stock stock = new Stock(0, "Stock Test", 10.0, 0);
		stock = service.add(stock);
		
		Stock savedStock = service.get(stock.getId());
		
		assertEquals(stock, savedStock);
	}
	
	@Test(expected=InvalidStockException.class)
	public void saveNullValue() throws InvalidStockException, StockNotFoundException {
		service.save(null);
	}
	
	@Test(expected=InvalidStockException.class)
	public void saveStockWithNullName() throws InvalidStockException, StockNotFoundException {
		Stock stock = new Stock(0, "Stock Test", 10.0, 0);
		stock = service.add(stock);
		
		Stock updatedStock = new Stock(stock.getId(), null, 10.0, 0);
		service.save(updatedStock);
	}
	
	@Test(expected=InvalidStockException.class)
	public void saveStockWithNegavitePrice() throws InvalidStockException, StockNotFoundException {
		Stock stock = new Stock(0, "Stock Test", 10.0, 0);
		stock = service.add(stock);
		
		Stock updatedStock = new Stock(stock.getId(), "Stock Test", -10.0, 0);
		service.save(updatedStock);
	}
}
