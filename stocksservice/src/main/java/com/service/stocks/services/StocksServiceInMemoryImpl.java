package com.service.stocks.services;

import java.util.Collection;
import org.springframework.stereotype.Service;
import com.service.stocks.model.Stock;
import com.service.stocks.repositories.StocksRepository;
import com.service.stocks.repositories.exceptions.InvalidIdException;
import com.service.stocks.services.exceptions.InvalidStockPriceException;
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
        return repository.getAll();
    }

    @Override
    public Stock get(long id) throws StockNotFoundException {
        try {
            return repository.get(id);
        } catch (InvalidIdException e) {
            throw new StockNotFoundException();
        }
    }

    @Override
    public Stock add(Stock stock) throws InvalidStockPriceException {
        if (stock == null || stock.getName() == null || stock.getCurrentPrice() < 0) {
            throw new InvalidStockPriceException();
        }

        return repository.add(stock);
    }

    @Override
    public void addAll(Collection<Stock> stocks) {
        repository.addAll(stocks);
    }

    @Override
    public Stock updatePrice(long id, double newPrice) throws StockNotFoundException, InvalidStockPriceException {
        
        if(newPrice < 0) {
            throw new InvalidStockPriceException();
        }
        
        try {
            return repository.updatePrice(id, newPrice);
        } catch (InvalidIdException e) {
            throw new StockNotFoundException();
        }
    }

}
