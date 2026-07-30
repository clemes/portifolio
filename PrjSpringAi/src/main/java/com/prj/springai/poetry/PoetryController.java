package com.prj.springai.poetry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class PoetryController {
	private static final Logger log = LogManager.getLogger(PoetryController.class);
	
	private PoetryService poetryService;

	public PoetryController(PoetryService poetryService) {
		this.poetryService = poetryService;
	}
	
	@GetMapping("/gen-poem")
	String generate() {
		log.info("Generating poem without request data, returning content from LLM model");
		return this.poetryService.generate();
	}
	
	@PostMapping("/poems")
	ResponseEntity<Poem> generate(@RequestBody PoemGenerationRequest request){
		log.info("Generating poem with request: {}", request);
		Poem response = poetryService.generate(request.genre, request.theme);
		return ResponseEntity.ok(response);
	}
	
	record PoemGenerationRequest(String genre, String theme) {}
}
