package com.service.stocks.repositories;

import java.util.Collection;

import org.springframework.context.annotation.Profile;

import com.service.stocks.model.Stock;
import com.service.stocks.repositories.exceptions.InvalidIdException;

public interface StocksRepository {
	void addAll(Collection<Stock> stocksToBeAdded);
	Stock add(Stock stock);
	Stock save(Stock stock) throws InvalidIdException;
	Collection<Stock> getAll();
	Stock get(long id) throws InvalidIdException;
}
