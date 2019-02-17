package com.service.stocks.config;

import java.util.Collection;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.service.stocks.model.Stock;

//@Configuration
//@ConfigurationProperties("stocks")
public class StocksConfiguration {
    private Collection<Stock> load;
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Stock> getLoad() {
		return load;
	}

	public void setLoad(Collection<Stock> load) {
		this.load = load;
	}
}
