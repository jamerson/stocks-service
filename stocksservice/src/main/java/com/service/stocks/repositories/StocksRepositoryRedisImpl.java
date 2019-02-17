package com.service.stocks.repositories;

import java.util.Collection;

import org.springframework.data.redis.core.RedisTemplate;

import com.service.stocks.model.Stock;
import com.service.stocks.repositories.exceptions.InvalidIdException;

public class StocksRepositoryRedisImpl implements StocksRepository {
	
	private RedisTemplate<Long,Stock> redis;
	
	public StocksRepositoryRedisImpl(RedisTemplate<Long,Stock> redis) {
        this.redis = redis;
    }
	
	@Override
	public void addAll(Collection<Stock> stocksToBeAdded) {
		

	}

	@Override
	public Stock add(Stock stock) {
		redis.opsForValue().set(stock.getId(), stock);
		
		return stock;
	}

	@Override
	public Stock save(Stock stock) throws InvalidIdException {
		redis.opsForValue().set(stock.getId(), stock);
		
		return stock;
	}

	@Override
	public Collection<Stock> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stock get(long id) throws InvalidIdException {
		// TODO Auto-generated method stub
		return null;
	}

}
