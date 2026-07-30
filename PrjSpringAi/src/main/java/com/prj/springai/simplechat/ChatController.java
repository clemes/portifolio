package com.prj.springai.simplechat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
	private static final Logger log = LogManager.getLogger(ChatController.class);
	private SimpleChatService aiChat;

	public ChatController(SimpleChatService simpleChat) {
		this.aiChat = simpleChat;
	}

	@GetMapping("/ask")
	public String ask(@RequestParam String message) {
		log.info("Asking AiModel: {}", message);
		return this.aiChat.promptAi(message);
	}
}
