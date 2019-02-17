package com.service.stocks.config;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.service.stocks.model.Stock;

@Configuration
@ConfigurationProperties("stocks")
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

	public void setLoad(Map<String, ConfigStock> fromConfig) {
		this.load = fromConfig.values().stream().map(item -> {
			return new Stock(
					item.getId(), 
					item.getName(), 
					item.getCurrentPrice(), 
					item.getLastUpdate());
		}).collect(Collectors.toList());
	}
	
	public static class ConfigStock {
		private long id;
		private String name;
		private double currentPrice;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getCurrentPrice() {
			return currentPrice;
		}
		public void setCurrentPrice(double currentPrice) {
			this.currentPrice = currentPrice;
		}
		public long getLastUpdate() {
			return lastUpdate;
		}
		public void setLastUpdate(long lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
		private long lastUpdate;
	}
}
