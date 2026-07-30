package com.prj.springai.piotrblog.controllers;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prj.springai.piotrblog.model.Person;

@RestController
@RequestMapping("/persons")
public class PersonController {
	private static final Logger log = LogManager.getLogger(PersonController.class);

	private final ChatClient chatClient;

	public PersonController(ChatClient.Builder chatClient, ChatMemory chatMemory) {
		this.chatClient = chatClient
				.defaultAdvisors(
						PromptChatMemoryAdvisor.builder(chatMemory).build(),
						SimpleLoggerAdvisor.builder().build()
						)
				.build();
	}
	
	@GetMapping
	List<Person> findAll(){
		PromptTemplate pt = new PromptTemplate("""
				Return a current list of 10 famous persons if exists or generate a new list with random values.
                Each object should contain an auto-incremented id field.
                The age value should be a number between 18 and 99.
                Do not include any explanations or additional text. 
				""");
			//        Return data in RFC8259 compliant JSON format.
		
//		CallResponseSpec responseSpec = this.executePrompt(pt.create());
//		
//		log.info("[findAll] Response from AiModel: /n {}", responseSpec.content());
//		
//		return responseSpec.entity(new ParameterizedTypeReference<>() {});
		Prompt p = pt.create();
		log.info("\n[findAll]\n{}", p);
		
		return this.chatClient
				.prompt(p)
				.call()
				.entity(new ParameterizedTypeReference<>() {});
	}
	
	@GetMapping("/{id}")
    Person findById(@PathVariable String id) {
        PromptTemplate pt = new PromptTemplate("""
                Find and return the person with id {id} in the current list of famous persons.
                """);
        Prompt p = pt.create(Map.of("id", id));
		log.info("\n[findById]\n{}", p);
        
        return this.chatClient.prompt(p)
                .call()
                .entity(Person.class);
//		CallResponseSpec responseSpec = this.executePrompt(p);
//		log.info("[findById] Response from AiModel: /n {}", responseSpec.content());
//		
//        return responseSpec.entity(Person.class);
    }
	
//	private CallResponseSpec executePrompt(Prompt pt) {
//		return this.chatClient
//				.prompt(pt)
//				.call();
//	}
}
