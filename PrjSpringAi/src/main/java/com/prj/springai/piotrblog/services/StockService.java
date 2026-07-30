package com.prj.springai.piotrblog.services;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.prj.springai.piotrblog.services.twelvedata.api.DailyStockData;
import com.prj.springai.piotrblog.services.twelvedata.api.StockApiProperties;
import com.prj.springai.piotrblog.services.twelvedata.api.StockData;

@Service
public class StockService {
	private static final Logger log = LogManager.getLogger(StockService.class);
	
	private static final String STOCK_PRICE_LAST_INTERVAL = "1min";
	private static final String STOCK_PRICE_LAST_SIZE = "1";
	private static final String STOCK_PRICE_HISTORY_INTERVAL = "1day";
	
	private final RestTemplate restTemplate;
	private final StockApiProperties apiProps;
		
	public StockService(RestTemplate restTemplate, StockApiProperties apiProps) {
		this.restTemplate = restTemplate;
		this.apiProps = apiProps;
	}
	
	public Double searchLastStockPrice(String companySymbol) {
		StockData data = this.retrieveStockDataObject(companySymbol, STOCK_PRICE_LAST_INTERVAL, STOCK_PRICE_LAST_SIZE);
        DailyStockData latestData = data.getValues().get(0);
        log.info("Today's stock price: {} -> {}", companySymbol, latestData.getClose());
        return Double.parseDouble(latestData.getClose());
	}

	public List<String> searchStockPriceHistory(String companySymbol, String historySize){
		StockData data = this.retrieveStockDataObject(companySymbol, STOCK_PRICE_HISTORY_INTERVAL, historySize);
        if (data != null && data.getValues() != null) {
        	log.info("Stock history found: {} -> {}", companySymbol, data.getValues().size());
            return data.getValues().stream().map(DailyStockData::getClose).toList();
        }
    	log.info("No Stock history found: {}", companySymbol);
        return Collections.emptyList();
	}
	
	public StockData searchHistoricalStockPrices(String companySymbol, Integer days) {
		return this.retrieveStockDataObject(companySymbol, STOCK_PRICE_HISTORY_INTERVAL, days.toString());
	}
	
	private StockData retrieveStockDataObject(String symbol, String interval, String size) {
		// URL template: https://api.twelvedata.com/time_series?symbol={0}&interval={1}&outputsize={2}&apikey={3}
		return restTemplate.getForObject(this.apiProps.getUrl(),
                StockData.class,
                symbol,
                interval,
                size,
                this.apiProps.getKey());
	}
}
