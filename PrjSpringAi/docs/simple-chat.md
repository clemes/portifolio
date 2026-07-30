# Simple Chat

Very basic implementation of a ChatClient using Ollama model.
The purpose of this implementation is to simply check if all dependencies and configuration were correctly in place.
But also, if the application was able to run and get response from AI model.

It was defined a simple controller and one service, respectively [ChatController](../src/main/java/com/prj/springai/simplechat/ChatController.java) and [SimpleChatService](../src/main/java/com/prj/springai/simplechat/SimpleChatService.java).

## ChatController
Contains a reference to the chat service (_aiChat_), injected by SpringBoot.
Defines the Rest API **"/chat/ask"** using the springframework anotations.
It invokes the chat service to execute the prompt towards the AI model.

## SimpleChatService
Contains a reference to ChatClient (_apiClient_) which is the SpringAI interface between the application and the AI model.
SpringAI uses the configuration of the project to automatically connect to the ai model running in the defined server.

The service receives the prompt provided by the user in the Rest API, define as prompt in the ChatClient (method: _prompt_), calls the model (method: _call_), and return its content (method: _content_).
The return is a string response from the model without any type of manipulation.

