package com.service.stocks.repositories;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import com.service.stocks.model.Stock;
import com.service.stocks.repositories.exceptions.InvalidIdException;

@Repository
@Profile("redis")
public class StocksRepositoryRedisImpl implements StocksRepository {
    
    private final RedisTemplate<String, Stock> redisTemplate;
    private ValueOperations<String, Stock> ops;
    private RedisAtomicLong idGenerator;
    
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    
    public StocksRepositoryRedisImpl(RedisTemplate<String, Stock> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @PostConstruct
    private void init() {
        ops = redisTemplate.opsForValue();
        idGenerator = new  RedisAtomicLong("idGenerator", redisTemplate.getConnectionFactory());
    }

    @Override
    public void addAll(Collection<Stock> stocksToBeAdded) {
        Map<String, Stock> map = stocksToBeAdded
                .stream()
                .collect(Collectors.toMap(i -> String.valueOf(i.getId()), i -> i));
        ops.multiSet(map);
    }

    @Override
    public Stock add(Stock stock) {
        Stock stockWithId = new Stock(
                idGenerator.incrementAndGet(), 
                stock.getName(), 
                stock.getCurrentPrice(), 
                getCurrentTimestamp());
        ops.setIfAbsent(String.valueOf(stockWithId.getId()), stockWithId);
        return stockWithId;
    }

    @Override
    public Stock save(Stock stock) throws InvalidIdException {
        Stock updatedStock = new Stock(
                stock.getId(), 
                stock.getName(), 
                stock.getCurrentPrice(), 
                getCurrentTimestamp());
        boolean result = ops.setIfPresent(String.valueOf(updatedStock.getId()), updatedStock);
        if(!result)
            throw new InvalidIdException();
        return updatedStock;
    }

    @Override
    public Collection<Stock> getAll() {
        Set<String> keys = redisTemplate.keys("*");
        return ops.multiGet(keys);
    }

    @Override
    public Stock get(long id) throws InvalidIdException {
        return ops.get(id);
    }

}
