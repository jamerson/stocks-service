package com.service.stocks.services;

import java.util.Collection;

import com.service.stocks.model.Stock;
import com.service.stocks.services.exceptions.InvalidStockPriceException;
import com.service.stocks.services.exceptions.StockNotFoundException;

public interface StocksService {
	Stock get(long id) throws StockNotFoundException;
	Collection<Stock> getStocks();
	Stock add(Stock stock) throws InvalidStockPriceException;
	Stock updatePrice(long id, double newPrice) throws StockNotFoundException, InvalidStockPriceException;
	void addAll(Collection<Stock> load);
}
