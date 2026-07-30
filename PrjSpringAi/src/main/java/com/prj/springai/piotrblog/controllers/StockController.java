package com.prj.springai.piotrblog.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prj.springai.piotrblog.services.StockService;
import com.prj.springai.piotrblog.services.twelvedata.api.Stock;

@RestController
@RequestMapping("/stocks")
public class StockController {
	private static final Logger log = LogManager.getLogger(StockController.class);
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	private final ChatClient chatClient;
    private final VectorStore store;
    private final StockService stockService;
    
    private final RewriteQueryTransformer.Builder rqtBuilder;
    private final OllamaChatOptions options;

    public StockController(ChatClient.Builder chatClientBuilder,
    					   @Autowired(required = false) VectorStore store,
                           StockService stockService) {
    	this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.store = store;
        this.stockService = stockService;
        
        this.rqtBuilder = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder);
        this.options = OllamaChatOptions.builder()
//        	    .model("ministral-3:8b")
        	    .temperature(0.2)
        	    .topK(40)
        	    .topP(0.9)
        	    .numPredict(200)   // same as max_tokens
        	    .build();
    }

    @GetMapping("/load-data")
    void load() throws JsonProcessingException {
        final List<String> companies = List.of("AAPL", "MSFT", "GOOG", "AMZN", "META", "NVDA");
        for (String company : companies) {
        	var list = this.stockService.searchStockPriceHistory(company, "20");
        	if(!list.isEmpty()) {
        		var doc = Document.builder()
                        .id(UUID.randomUUID().toString())
                        .text(mapper.writeValueAsString(new Stock(company, list)))
//                        .text("""
//                        		Company: %s
//                        		Stock historical prices: %s
//                        	  """.formatted(company, list))
                        .metadata(Map.of("company", company))
                        .build();
                store.add(List.of(doc));
                log.info("Document added: {}", company);
        	}
        	
//            StockData data = restTemplate.getForObject("https://api.twelvedata.com/time_series?symbol={0}&interval=1day&outputsize=10&apikey={1}",
//                    StockData.class,
//                    company,
//                    apiKey);
//            if (data != null && data.getValues() != null) {
//                var list = data.getValues().stream().map(DailyStockData::getClose).toList();
//                var doc = Document.builder()
//                        .id(UUID.randomUUID().toString())
//                        .text(mapper.writeValueAsString(new Stock(company, list)))
//                        .metadata(Map.of("company", company))
//                        .build();
//                store.add(List.of(doc));
//                log.info("Document added: {}", company);
//            }
        }
        log.info("Load-data finished.");
    }
    
    @GetMapping("/docs")
    List<Document> query() {
        SearchRequest searchRequest = SearchRequest.builder()
                .query("Find the most growth trends")
                .topK(2)
                .build();
        List<Document> docs = store.similaritySearch(searchRequest);
        return docs;
    }
    /*************************************************************************************/
    @RequestMapping("/v1/most-growth-trend")
    String getBestTrend() {
        PromptTemplate pt = new PromptTemplate("""
                {query}.
                Which {target} is the most % growth?
                Use the information provided in the vector store defined in the provided question advisor.
                The 0 element in the prices table is the latest price, while the last element is the oldest price.
                """);

        Prompt p = pt.create(
                Map.of("query", "Find the most growth trends",
                       "target", "share in the store")
        );
		log.info("[getBestTrend-V1]\n{}", p);

        return this.chatClient.prompt(p)
        		.options(options)
                .advisors(QuestionAnswerAdvisor.builder(store).build())
                .call()
                .content();
    }
    /*************************************************************************************/
    @RequestMapping("/v2/most-growth-trend")
    String getBestTrendV2() {
//        PromptTemplate pt = new PromptTemplate("""
//                Which share in the prompt context is the most % growth?
//                Use the information stored in the vector store provided in the question advisor.
//                The 0 element in the prices table is the latest price, while the last element is the oldest price.
//                Return a full name of company instead of a market shortcut.
//                """);
        //stronger prompt
        PromptTemplate pt = new PromptTemplate("""
        		Given the CONTEXT with stock data.

        		TASK:
        		- Determine which company has the highest percentage growth.
        		- The first price (index 0) is the latest price.
        		- The last price is the oldest price.
        		- Compute percentage growth = (latest - oldest) / oldest * 100.
        		- Return the full company name.

        		If the answer cannot be determined from the context, say "UNKNOWN".
        		""");

        SearchRequest searchRequest = SearchRequest.builder()
                .query("""
                Find the most stock prices growth percentage latest vs oldest company performance.
                Use the information stored in the vector store and provided them to the question advisor as a prompt context.
                The 0 element in the prices table is the latest price, while the last element is the oldest price.
                """)
                .topK(3)
                .similarityThreshold(0.4)
                .build();

        Prompt p = pt.create();
		log.info("[getBestTrend-V2]\n{}\n{}", p, searchRequest);
		
//		var results = store.similaritySearch(searchRequest);
//		log.info("Results from store: {}", results);

        return this.chatClient.prompt(p)
                .advisors(QuestionAnswerAdvisor.builder(store).searchRequest(searchRequest).build())
                .call()
                .content();
    }
    /*************************************************************************************/
    @RequestMapping("/v3/most-growth-trend")
    String getBestTrendV3() {
        PromptTemplate pt = new PromptTemplate("""
                {query}.
                Which {target} is the most % growth?
                Use the information defined in the prompt context provided question advisor.
                The 0 element in the prices table is the latest price, while the last element is the oldest price.
                """);

        Prompt p = pt.create(Map.of("query", "Find the most growth trends", "target", "share"));

        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.4)
                        .topK(3)
                        .vectorStore(store)
                        .build())
                .queryTransformers(rqtBuilder.promptTemplate(pt).build())
                .build();

		log.info("[getBestTrend-V3]\n{}\n{}", p, retrievalAugmentationAdvisor);

        return this.chatClient.prompt(p)
                .advisors(retrievalAugmentationAdvisor)
                .call()
                .content();
    }
    /*************************************************************************************/
}
