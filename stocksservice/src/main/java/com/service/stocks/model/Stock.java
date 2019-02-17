package com.service.stocks.model;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("stock")
public class Stock {
	private final long id;
	private final String name;
	private final double currentPrice;
	private final long lastUpdate;

	public Stock(long id, String name, double currentPrice, long lastUpdate) {
		super();
		this.id = id;
		this.name = name;
		this.currentPrice = currentPrice;
		this.lastUpdate = lastUpdate;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public String toString() {
		return "Stock [id=" + id + ", name=" + name + ", currentPrice=" + currentPrice + ", lastUpdate=" + lastUpdate
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stock other = (Stock) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public Stock copy(long newId) {
		return new Stock(newId, getName(), getCurrentPrice(), getLastUpdate());
	}
	
}
