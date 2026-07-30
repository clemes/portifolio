package com.prj.springai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Article: https://spring.io/guides/gs/spring-boot/
 * 
 * <p> {@link RestController} indicates this class is ready for use by Spring MVC to handle web requests.
 *  It combines {@link Controller} and {@link ResponseBody} necessary for a web request that returns data rather than a view.
 * 
 * <p> {@link GetMapping} creates a REST call naming <i>greeting</i> and map it to the method {@linkplain HelloController#greeting()}
 * 
 * <p> Method {@linkplain HelloController#greeting()} simply returns plain text.
 * 
 * @author Cristiano
 *
 */
@RestController
public class HelloController {
	private static final Logger log = LogManager.getLogger(HelloController.class);

	/**
	 * Simple HTTP Get Request - {@link GetMapping}.
	 * 
	 * @return plain text.
	 */
	@GetMapping("/greeting")
	public String greeting() {
		log.info("Handling REST call /greeting");
		return "Greetings from first application example! No Ai model.";
	}
	
}
