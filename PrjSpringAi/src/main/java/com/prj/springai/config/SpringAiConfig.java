package com.prj.springai.config;

import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestTemplate;

import com.prj.springai.piotrblog.functions.stock.StockFunction;
import com.prj.springai.piotrblog.functions.stock.StockRequest;
import com.prj.springai.piotrblog.functions.stock.StockResponse;
import com.prj.springai.piotrblog.functions.wallet.WalletFunction;
import com.prj.springai.piotrblog.functions.wallet.WalletResponse;
import com.prj.springai.piotrblog.services.StockService;
import com.prj.springai.piotrblog.services.WalletService;

@Configuration
public class SpringAiConfig {

	@Bean
	ChatMemory chatMemory() {
		return MessageWindowChatMemory.builder()
				.chatMemoryRepository(new InMemoryChatMemoryRepository())
				.maxMessages(10)
				.build();
	}

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	@Bean
	@Description("Number of shares for each company in my portfolio")
	public Supplier<WalletResponse> numberOfShares(WalletService walletService) {
	    return new WalletFunction(walletService);
	}

	@Bean
	@Description("Latest stock prices")
	public Function<StockRequest, StockResponse> latestStockPrices(StockService stockService) {
	    return new StockFunction(stockService);
	}

}
