package com.service.stocks.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.stocks.model.Stock;
import com.service.stocks.services.StocksService;
import com.service.stocks.services.exceptions.InvalidStockException;
import com.service.stocks.services.exceptions.StockNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(StocksController.class)
public class StocksControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
    private StocksService service;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void getAllStocks() throws Exception {
		
		List<Stock> expected = Arrays.asList(
				new Stock(1, "Stock 1", 10, 0), 
				new Stock(2, "Stock 2", 20, 0),
				new Stock(3, "Stock 3", 30, 0));
		
		when(service.getStocks()).thenReturn(expected);
		
		this.mockMvc.perform(get("/stocks"))
	        .andExpect(status().isOk())
	        .andExpect(content()
	            .string(mapper.writeValueAsString(expected)));
	}
	
	@Test
	public void getStockById() throws Exception {
		
		Stock stock = new Stock(1, "Stock 1", 10, 0); 
		
		when(service.get(1)).thenReturn(stock);
		
		this.mockMvc.perform(get("/stocks/1"))
	        .andExpect(status().isOk())
	        .andExpect(content()
	        		.string(mapper.writeValueAsString(stock)));
	}
	
	@Test
	public void getUnknowId() throws Exception {
		when(service.get(4)).thenThrow(StockNotFoundException.class);
		
		this.mockMvc.perform(get("/stocks/4"))
        	.andExpect(status().isNotFound());
	}
	
	@Test
	public void addNewStock() throws Exception {
		Stock stock = new Stock(1, "Stock 1", 10, 0);
		
		when(service.add(stock)).thenReturn(stock);
		
		this.mockMvc.perform(post("/stocks")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(stock)))
	        .andExpect(status().isCreated());
	}
	
	@Test
	public void addEmptyPayload() throws Exception {
		
		this.mockMvc.perform(post("/stocks")
			.contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isBadRequest());
	}
	
	@Test
	public void addInvalidPayload() throws Exception {
		
		Stock stock = new Stock(0, null, 0, 0); 
		
		when(service.add(stock)).thenThrow(InvalidStockException.class);
		
		this.mockMvc.perform(post("/stocks")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{}"))
	        .andExpect(status().isBadRequest());
	}
	
	@Test
	public void addInvalidStock() throws Exception {
		
		Stock stock = new Stock(0, null, 0, 0);
		
		when(service.add(stock)).thenThrow(InvalidStockException.class);
		
		this.mockMvc.perform(post("/stocks")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(stock)))
	        .andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateStock() throws Exception {
		Stock stock = new Stock(1, "Stock 1", 10, 0);
		
		when(service.save(stock)).thenReturn(stock);
		
		this.mockMvc.perform(put("/stocks/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(stock)))
        	.andExpect(status().isOk());
	}
	
	@Test
	public void updateEmptyPayload() throws Exception {
		
		this.mockMvc.perform(put("/stocks/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateInvalidPayload() throws Exception {
		
		Stock stock = new Stock(1, null, 0, 0);
		
		when(service.save(stock)).thenThrow(InvalidStockException.class);
		
		this.mockMvc.perform(put("/stocks/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(stock)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateUnknowStock() throws Exception {
		Stock stock = new Stock(1, "Stock 1", 10, 0);
		
		when(service.save(stock)).thenThrow(StockNotFoundException.class);
		
		this.mockMvc.perform(put("/stocks/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(stock)))
			.andExpect(status().isNotFound());
	}
}
