package com.service.stocks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.stocks.model.Stock;

public interface StocksRepository extends JpaRepository<Stock, Long> {
	
}
