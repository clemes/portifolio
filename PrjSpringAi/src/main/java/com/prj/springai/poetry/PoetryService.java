package com.prj.springai.poetry;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class PoetryService {
	private static final Logger log = LogManager.getLogger(PoetryService.class);
	private final PromptTemplate promptTemplate = new PromptTemplate("Write a {genre} haiku about {theme} following the traditional 5-7-5 syllable structure.");
	
	ChatClient apiClient;

	public PoetryService(ChatClient.Builder apiClientBuilder) {
		this.apiClient = apiClientBuilder.build();
	}
	
	public String generate() {
		return apiClient
			      .prompt("Write a playful haiku about morning coffee following the traditional 5-7-5 syllable structure.")
			      .call()
			      .content();
	}

	public Poem generate(String genre, String theme) {		
		Prompt prompt = this.promptTemplate.create(Map.of(
		        "genre", genre,
		        "theme", theme));
		log.info("Poetry prompt: {}", prompt);
		
		return this.apiClient
				.prompt(prompt)
				.call()
				.entity(Poem.class);
	}
}
