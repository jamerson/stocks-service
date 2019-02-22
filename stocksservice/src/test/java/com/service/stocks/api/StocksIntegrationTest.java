package com.service.stocks.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.service.stocks.model.Stock;

@ActiveProfiles("redis")
@RunWith(SpringRunner.class)
@WebMvcTest(StocksController.class)
public class StocksIntegrationTest {
    
    TestRestTemplate restTemplate = new TestRestTemplate();
    
    HttpHeaders headers = new HttpHeaders();
    
    @Autowired
    private MockMvc mockMvc;
    
    ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void getAllStocks() throws Exception {
        
        String json = this.mockMvc.perform(get("/stocks")).andReturn().getResponse().getContentAsString();
        
        List<String> names = JsonPath.read(json, "$[*].name");
        
        assertTrue(names.contains("Stock1"));
        assertTrue(names.contains("Stock2"));
        assertTrue(names.contains("Stock3"));
        
        List<Double> prices = JsonPath.read(json, "$[*].currentPrice");
        
        assertTrue(prices.contains(10D));
        assertTrue(prices.contains(20D));
        assertTrue(prices.contains(30D));
    }
    
    @Test
    public void addRetrieveAndUpdate() throws Exception {
        Stock stock = new Stock(1, "Stock 100", 35D, 0);
        
        String json = this.mockMvc.perform(
                post("/stocks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(stock)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        
        assertEquals("Stock 100", JsonPath.read(json, "$.name"));
        assertTrue((double)JsonPath.read(json, "$.currentPrice") == 35D);
        
        int id = JsonPath.read(json, "$.id");
        long lastUpdate = JsonPath.read(json, "$.lastUpdate");
        
        json = this.mockMvc.perform(
                get("/stocks/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        
        assertEquals("Stock 100", JsonPath.read(json, "$.name"));
        assertTrue(35D == (double)JsonPath.read(json, "$.currentPrice"));
        assertTrue(id == (int)JsonPath.read(json, "$.id"));
        assertTrue(lastUpdate == (long)JsonPath.read(json, "$.lastUpdate"));
        
        Stock updateStock = new Stock(Long.valueOf(id), "Updated Stock 100", 44D, Long.valueOf(lastUpdate));
        
        json = this.mockMvc.perform(
                put("/stocks/" + id).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(updateStock)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        
        assertEquals("Stock 100", JsonPath.read(json, "$.name"));
        assertTrue(44D == (double)JsonPath.read(json, "$.currentPrice"));
        assertTrue(id == (int)JsonPath.read(json, "$.id"));
        assertTrue((long)JsonPath.read(json, "$.lastUpdate") > lastUpdate);
        
    }
    
    @Test
    public void updateUnknowStock() throws Exception {
        Stock stock = new Stock(1, "Stock 200", 35D, 0);
        this.mockMvc.perform(
                put("/stocks/2000").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(stock)))
                .andExpect(status().isNotFound());
    }

}
