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
@Profile("redis, !map")
public class StocksRepositoryRedisImpl implements StocksRepository {
    
    private final RedisTemplate<String, Stock> redisTemplate;
    private ValueOperations<String, Stock> ops;
    private RedisAtomicLong idGenerator;
    private final String KEY = "stock:";
    
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
                .collect(Collectors.toMap(i -> KEY + String.valueOf(idGenerator.incrementAndGet()), i -> i));
        ops.multiSet(map);
    }

    @Override
    public Stock add(Stock stock) {
        stock.setId(idGenerator.incrementAndGet());
        stock.setLastUpdate(getCurrentTimestamp());
        ops.setIfAbsent(KEY + String.valueOf(stock.getId()), stock);
        return stock;
    }

    @Override
    public Stock updatePrice(long id, double newPrice) throws InvalidIdException {
        Stock stock = get(id);
        if(stock == null) {
            throw new InvalidIdException();
        }
        stock.setLastUpdate(getCurrentTimestamp());
        stock.setCurrentPrice(newPrice);
        boolean result = ops.setIfPresent(KEY + String.valueOf(stock.getId()), stock);
        
        if(!result)
            throw new InvalidIdException();
        
        return stock;
    }

    @Override
    public Collection<Stock> getAll() {
        Set<String> keys = redisTemplate.keys(String.format("%s*", KEY));
        return ops.multiGet(keys);
    }

    @Override
    public Stock get(long id) throws InvalidIdException {
        return ops.get(KEY + id);
    }

}
