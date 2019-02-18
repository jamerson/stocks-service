package com.service.stocks.services;

import java.util.Collection;

import com.service.stocks.model.Stock;
import com.service.stocks.services.exceptions.InvalidStockException;
import com.service.stocks.services.exceptions.StockNotFoundException;

public interface StocksService {
	Stock get(long id) throws StockNotFoundException;
	Collection<Stock> getStocks();
	Stock add(Stock stock) throws InvalidStockException;
	Stock save(Stock stock) throws StockNotFoundException, InvalidStockException;
	void addAll(Collection<Stock> load);
}
