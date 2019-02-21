package com.service.stocks.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
import com.service.stocks.repositories.StocksRepositoryConcurrentHashmapImpl;
import com.service.stocks.services.exceptions.InvalidStockPriceException;
import com.service.stocks.services.exceptions.StockNotFoundException;;

@ActiveProfiles("map")
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
	public void addNewStock() throws InvalidStockPriceException {
		Stock stock = new Stock(0, "Stock Test", 10.0, 0);
		stock = service.add(stock);
		
		assertNotEquals(0, stock.getId());
		assertNotEquals(0, stock.getLastUpdate());
	}
	
	@Test(expected=InvalidStockPriceException.class)
	public void addNullValue() throws InvalidStockPriceException {
		service.add(null);
	}
	
	@Test(expected=InvalidStockPriceException.class)
	public void addStockWithNullName() throws InvalidStockPriceException {
		Stock stock = new Stock(0, null, 10.0, 0);
		stock = service.add(stock);
	}
	
	@Test(expected=InvalidStockPriceException.class)
	public void addStockWithNegavitePrice() throws InvalidStockPriceException {
		Stock stock = new Stock(0, null, -10.0, 0);
		stock = service.add(stock);
	}
	
	@Test
	public void getSavedStock() throws StockNotFoundException, InvalidStockPriceException {
		Stock stock = new Stock(0, "Stock Test", 10.0, 0);
		stock = service.add(stock);
		
		Stock savedStock = service.get(stock.getId());
		
		assertEquals(stock, savedStock);
	}
	
	@Test(expected=InvalidStockPriceException.class)
	public void saveStockWithNegavitePrice() throws InvalidStockPriceException, StockNotFoundException {
		Stock stock = new Stock(0, "Stock Test", 10.0, 0);
		stock = service.add(stock);
		
		service.updatePrice(stock.getId(), -10.0);
	}
}
