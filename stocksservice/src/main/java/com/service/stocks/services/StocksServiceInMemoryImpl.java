package com.service.stocks.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.service.stocks.config.StocksConfiguration;
import com.service.stocks.model.Stock;
import com.service.stocks.repositories.StocksRepository;
import com.service.stocks.repositories.exceptions.InvalidIdException;
import com.service.stocks.services.exceptions.InvalidStockException;
import com.service.stocks.services.exceptions.StockNotFoundException;

@Service
public class StocksServiceInMemoryImpl implements StocksService {
	
	private final StocksRepository repository;
	
	public StocksServiceInMemoryImpl(StocksRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Collection<Stock> getStocks() {
		return repository.findAll();
	}

	@Override
	public Stock get(long id) throws StockNotFoundException {
		return repository.findById(id).orElseThrow(StockNotFoundException::new);
		
	}

	@Override
	public Stock add(Stock stock) throws InvalidStockException {
		if(stock == null ||
			stock.getName() == null ||
			stock.getCurrentPrice() < 0) {
			throw new InvalidStockException();
		}
		
		return repository.save(stock);
	}

	@Override
	public Stock save(Stock stock) throws StockNotFoundException, InvalidStockException {
		if(stock == null ||
				stock.getName() == null ||
				stock.getCurrentPrice() < 0) {
				throw new InvalidStockException();
			}
		
		try {
			return repository.save(stock);
		} catch (InvalidIdException e) {
			throw new StockNotFoundException();
		}
	}

}
