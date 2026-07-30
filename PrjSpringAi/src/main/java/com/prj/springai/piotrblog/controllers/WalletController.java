package com.prj.springai.piotrblog.controllers;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prj.springai.piotrblog.tools.StockTool;
import com.prj.springai.piotrblog.tools.WalletTool;

@RestController
@RequestMapping("/wallet")
public class WalletController {
	private static final Logger log = LogManager.getLogger(WalletController.class);

	private final ChatClient chatClient;
	private final WalletTool walletTool;
	private final StockTool stockTool;

    public WalletController(ChatClient.Builder chatClientBuilder,
    		WalletTool walletTool,
    		StockTool stockTool) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
		this.walletTool = walletTool;
		this.stockTool = stockTool;
    }

    @GetMapping("/with-function")
    String calculateWalletValueByFunction() {
//        PromptTemplate pt = new PromptTemplate("""
//        What’s the current value in dollars of my wallet based on the latest stock daily prices ?
//        First get the shares in my wallet (one time call) and then calculate their value using today's stock prices.
//        """);

        /**
         * Currently I am using Ollama to run the AiModel locally.
         * Functions are not available for it.
         * Instead, it was implemented Tools to execute similar task as the functions.
         */
//        return this.chatClient.prompt(pt.create(
//        		OpenAiChatOptions.builder()
//                .function("numberOfShares")
//                .function("latestStockPrices")
//                .build()
//                        )
//        		)
//                .call()
//                .content();
        return "Option not available while using Ollama AI model.";
    }
    
    @GetMapping("/with-tools")
    String calculateWalletValue() {
        PromptTemplate pt = new PromptTemplate("""
        What’s the current value in dollars of my wallet based on the latest stock daily prices ?
        First get the shares in my wallet (one time call) and then calculate their value using today's stock prices.
        """);

		Prompt p = pt.create();
		log.info("[calculateWalletValue]\n{}", p);
        
        return this.chatClient
        		.prompt(p)
        		.tools(this.walletTool, this.stockTool)
        		.call()
        		.content();
    }
    
    @GetMapping("/highest-day/{days}")
    String calculateHighestWalletValue(@PathVariable int days) {
        PromptTemplate pt = new PromptTemplate("""
        On which day during last {days} days my wallet had the highest value in dollars based on the historical daily stock prices ?
        """);

        return this.chatClient.prompt(pt.create(Map.of("days", days)))
                .tools(stockTool, walletTool)
                .call()
                .content();
    }
}
