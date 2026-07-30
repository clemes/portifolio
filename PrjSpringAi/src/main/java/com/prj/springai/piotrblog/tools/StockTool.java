package com.prj.springai.piotrblog.tools;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.prj.springai.piotrblog.functions.stock.StockResponse;
import com.prj.springai.piotrblog.services.StockService;
import com.prj.springai.piotrblog.services.twelvedata.api.DailyShareQuote;
import com.prj.springai.piotrblog.services.twelvedata.api.StockData;

@Component
public class StockTool {
	private static final Logger log = LogManager.getLogger(StockTool.class);

	private StockService stockService;

	public StockTool(StockService stockService) {
		this.stockService = stockService;
	}
	
//	@Tool(description = "Today's stock price for one company")
//	public StockResponse getLatestStockPrices(@ToolParam(description = "Symbol of company") String company) {
//		log.info("Get stock prices for: {}", company);
//		if (company.matches("\\[[A-Z,\\s]+\\]")) {
//			List<String> list = Arrays.asList(
//					company.substring(1, company.length() - 1).split(",\\s*")
//				);
//		}
//		return this.stockService.apply(new StockRequest(company));
//	}

	@Tool(description = "Today's stock price for a list of companies")
	public List<StockResponse> getLatestStockPriceForListCompanies(@ToolParam(description = "List of companies symbol") List<String> companies){
		log.info("[StockTool] Tool triggered for list of companies: {}", companies);
		return companies.stream()
				.map(this::toStockResponse)
				.toList();
	}
	
	@Tool(description = "Latest stock prices for one company")
    public StockResponse getLatestStockPrices(@ToolParam(description = "Company symbol") String company) {
        return toStockResponse(company);
    }

    @Tool(description = "Historical daily stock prices")
    public List<DailyShareQuote> getHistoricalStockPrices(@ToolParam(description = "Search period in days") int days,
                                                          @ToolParam(description = "Name of company") String company) {
        StockData data = this.stockService.searchHistoricalStockPrices(company, days);
        return data.getValues().stream()
                .map(d -> new DailyShareQuote(company, Float.parseFloat(d.getClose()), d.getDatetime()))
                .toList();
    }
	
	private StockResponse toStockResponse(String symbol) {
		return new StockResponse(symbol, this.stockService.searchLastStockPrice(symbol));
	}
}
