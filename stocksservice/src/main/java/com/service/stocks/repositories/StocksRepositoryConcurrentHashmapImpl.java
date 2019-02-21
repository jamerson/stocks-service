package com.service.stocks.repositories;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.service.stocks.model.Stock;
import com.service.stocks.repositories.exceptions.InvalidIdException;

@Repository
@Profile("map")
public class StocksRepositoryConcurrentHashmapImpl implements StocksRepository {

    private final ConcurrentHashMap<Long, Stock> stocks = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    @Override
    public void addAll(Collection<Stock> stocksToBeAdded) {
        stocksToBeAdded.stream().forEach(stock -> stocks.putIfAbsent(stock.getId(), stock));
    }

    @Override
    public Stock add(Stock stock) {

        long stockId = idGenerator.incrementAndGet();
        long currentTs = getCurrentTimestamp();
        
        stock.setId(stockId);
        stock.setLastUpdate(currentTs);

        stocks.put(stockId, stock);

        return stock;
    }

    @Override
    public Stock save(Stock stock) throws InvalidIdException {
        long currentTs = getCurrentTimestamp();

        stock.setLastUpdate(currentTs);

        Stock result = stocks.replace(stock.getId(), stock);

        if (result == null)
            throw new InvalidIdException();

        return result;
    }

    @Override
    public Collection<Stock> getAll() {
        return stocks.values();
    }

    @Override
    public Stock get(long id) throws InvalidIdException {
        Stock result = stocks.get(id);

        if (result == null)
            throw new InvalidIdException();

        return result;
    }

}
