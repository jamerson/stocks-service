package com.service.stocks.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class Stock implements Serializable {

    private static final long serialVersionUID = -2468057406500509489L;
    
    private long id;
    private String name;
    private double currentPrice;
    private long lastUpdate;
    
    public Stock() {
        super();
    }

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

    public static long getSerialversionuid() {
        return serialVersionUID;
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
        result = prime * result + (int) id;
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

}
