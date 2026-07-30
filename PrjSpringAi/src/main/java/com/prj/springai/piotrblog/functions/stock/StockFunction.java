package com.prj.springai.piotrblog.functions.stock;

import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.prj.springai.piotrblog.services.StockService;

public class StockFunction implements Function<StockRequest, StockResponse>{
	private static final Logger log = LogManager.getLogger(StockFunction.class);
	
	private StockService stockService;
	
	public StockFunction(StockService stockService) {
		this.stockService = stockService;
	}

	@Override
	public StockResponse apply(StockRequest stockRequest) {
		log.info("[StockFunction] Function triggered with param: {}", stockRequest.company());
		Double stockPrice = this.stockService.searchLastStockPrice(stockRequest.company());
		return new StockResponse(stockRequest.company(), stockPrice);
	}

}
