package com.service.stocks.repositories;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.service.stocks.model.Stock;
import com.service.stocks.repositories.exceptions.InvalidIdException;

@Repository
public class StocksRepositoryMongoImpl implements StocksRepository {

	private MongoTemplate mongoTemplate;
	
	
	public StocksRepositoryMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
	
	@Override
	public void addAll(Collection<Stock> stocksToBeAdded) {
		mongoTemplate.insertAll(stocksToBeAdded);
	}

	@Override
	public Stock add(Stock stock) {
		stock = mongoTemplate.insert(stock);
		return stock;
	}

	@Override
	public Stock save(Stock stock) throws InvalidIdException {
		//stock = mongoTemplate.updateFirst(query, update, entityClass)(stock);
		//if(!result) throw new InvalidIdException();
		return stock;
	}

	@Override
	public Collection<Stock> getAll() {
		return mongoTemplate.findAll(Stock.class);
	}

	@Override
	public Stock get(long id) throws InvalidIdException {
		return mongoTemplate.findById(id, Stock.class);
	}

}
