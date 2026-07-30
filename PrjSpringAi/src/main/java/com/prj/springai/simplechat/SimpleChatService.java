package com.prj.springai.simplechat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SimpleChatService {
	ChatClient apiClient;

	public SimpleChatService(ChatClient.Builder apiClientBuilder) {
		this.apiClient = apiClientBuilder.build();
	}

	public String promptAi(String strPrompt) {
//		ChatClientRequestSpec requestSpec = apiClient.prompt(strPrompt);
//		CallResponseSpec responseSpec = requestSpec.call();
		// Method .chatResponse contains the full response from Ai model for the provided prompt.
//		System.out.println(responseSpec.chatResponse());
		
		//Method .content contains only the text response from AiModel.
//		return responseSpec.content();
		
		return this.apiClient
				.prompt(strPrompt)
				.call()
				.content();
	}
}
