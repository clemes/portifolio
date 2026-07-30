# AI Financial Assistant by Piotr Minkowski

References:
1. [Getting Started with Spring AI and Chat Model](https://piotrminkowski.com/2025/01/28/getting-started-with-spring-ai-and-chat-model/)
2. [Getting Started with Spring AI Function Calling](https://piotrminkowski.com/2025/01/30/getting-started-with-spring-ai-function-calling)
3. [Using RGA and Vector Store with Spring AI](https://piotrminkowski.com/2025/02/24/using-rag-and-vector-store-with-spring-ai/)
4. [Spring AI with Multimodality and Images](https://piotrminkowski.com/2025/03/04/spring-ai-with-multimodality-and-images/)
5. [Using Ollama with Spring AI](https://piotrminkowski.com/2025/03/10/using-ollama-with-spring-ai)
6. [Tool Calling with Spring AI](https://piotrminkowski.com/2025/03/13/tool-calling-with-spring-ai/)

This application was inspired by a series of Spring AI articles by Piotr Minkowski and explores several advanced AI integration concepts using Spring Boot and Spring AI.

The project includes AI-powered financial and portfolio-related features involving:
* Persons
* Stocks
* Wallets
* Portfolio analysis
* AI-assisted financial workflows

The tutorial also demonstrates how to switch between cloud AI providers and locally running AI models through Ollama.
Although this project uses mainly LLM models from Ollama.

## Reference-Based Features and Concepts

## 1. Spring AI Chat Models

This part of the project demonstrates:
* AI chat model integration
* Prompt engineering
* Structured AI output mapping
* Chat memory support
* Logging AI interactions

### Implemented Concepts
* ChatClient
* PromptTemplate
* AI-generated Person entities
* Structured response mapping with .entity()
* AI conversation memory

### Learning Outcome
I learned how Spring AI abstracts communication with LLMs and how structured AI responses can be integrated into backend applications.

## 2. AI Function Calling

This section focuses on AI-triggered backend operations.
It was used the model **llama3.1:8b**, which supports Tools.

### Features
* Wallet and stock management
* AI-triggered Java functions
* Portfolio value calculations
* External stock market API integration

### Implemented Concepts
* Function calling with Spring Beans
* Supplier and Function
* AI-assisted backend orchestration
* Dynamic financial calculations

Controller: [WalletController](../src/main/java/com/prj/springai/piotrblog/controllers/WalletController.java)

### Learning Outcome
This helped me understand how AI models can invoke backend business logic dynamically instead of only generating text responses.

Unfortunately, because of the limited resource, the implemented functions did not work in the AI model configured.
This looks like an implementation available in cloud solution, but not in Ollama.
Instead, it was used AI Tools to execute the same purpose.
The tools worked as expected, except when it was required multiple call for it.
However, that is a limitation of the AI models used in the project.

## 3. Retrieval-Augmented Generation (RAG)

This implementation introduces semantic search and context-aware AI responses.
It was used the model **ministral-3:8b**, which supports RAG.

### Features
* Vector database integration
* Context retrieval before AI prompting
* Semantic similarity search
* Improved AI response accuracy
* AI functions (not available for Ollama)
* AI Tools

### Implemented Concepts
* Pinecone vector store
* VectorStore
* QuestionAnswerAdvisor
* RetrievalAugmentationAdvisor

Controller: [StockController](../src/main/java/com/prj/springai/piotrblog/controllers/StockController.java)

### Learning Outcome
I learned how RAG architectures improve AI systems by providing relevant external knowledge and reducing hallucinations.

The experimental _RetrievalAugmentationAdvisor_ did not work with the setup used in this project.
This might be related to the low resource device used to run the AI model.

## 4. Multimodal AI and Image Processing

This module explores multimodality and image generation along with processing image description within AI workflows.

It was used **llava:7b** model, which it was the most suitable image model that runs under the configured device of this project.
However, this model turned out to not deliver answer for basic tasks, such as analyzing multi-images.
In fact, the model was not designed for that purpose.
Also, the accuracy of the response for describing an image was not satifactory.
After some research it was found that the models **qwen2.5vl** and **qwen3-vl** could possibly be alternative models.
The model **qwen2.5vl** was used for testing the implementation.

### Features
* Image analysis
* Object recognition
* AI-generation of images
* AI-generated image descriptions (VectorStore)
* Structured image-based responses

### Implemented Concepts
* Media
* UserMessage
* Multimodal prompts
* Image + text AI interactions

Controller: [ImageController](../src/main/java/com/prj/springai/piotrblog/controllers/ImageController.java)

### Learning Outcome
This helped me understand multimodal AI systems and how Spring AI supports image-enabled LLM interactions.

Because of the resource limitation, the implementation done at _ImageController_ did not work as expected.
Also, the response time was quite high.
To overcome this limitation, a better device which runs the AI model should be used.
This will be explored in another moment.

When using the model **llava:7b**, the default answer was _"empty list of images"_ when attempting to find an object in a list of images.
It was added a couple of API calls to investigate and explore different configurations in the attempt to fix this issue.
The conclusion was that this AI model does not support processing multi-image request.
The model **qwen3-vl** showed to not be a good choice either, because when tested isolated in the ollama server, it got in a infinite loop,
and it never returned a final answer.

On the other hand, the model **qwen2.5vl:7b** showed to be a good choice to explore the multi-image processing functionality.
Although the model took some time to responde, it mostly returned the correct answer.
In order to help the processing speed, the images were changed of dimensions.
This would make the low-resource device process less information.
It was possible to get a response in around 20 to 30 minutes.

It was also learned how to increase the number of context in a prompt call in Ollama.

Unfortunately, the setup used in this project, did not allow to test the implementation that generates image.
The ImageModel is not available for Ollama AI server (yet).
Also, the functionality using Vector Store was not tested, also due to resource limitation.


## 5. Local AI with Ollama

This part of the project demonstrates local LLM execution without relying on cloud AI providers.

### Features
* Local model execution using Ollama
* Switching between AI providers
* Running local models such as Llava and Granite
* Offline AI experimentation

### Implemented Concepts
* Ollama integration
* Maven profile-based provider switching
* Local AI deployment
* Provider abstraction with Spring AI
* Access to Ollama server located in a different device (via Network)

### Learning Outcome
I learned how to run and integrate local AI models into Spring Boot applications while maintaining provider-independent business logic.


## 6. Tool Calling in AI

This part of the project demonstrates how to extend LLM capabilities by allowing them to invoke application-defined tools (similar to functions) to retrieve external data and perform business operations.

### Features
* Tool calling (function calling) with Spring AI
* Integration with external REST APIs and business services
* Automatic tool selection by the LLM
* Multi-step reasoning with sequential tool execution
* Support for multiple AI providers (OpenAI, Mistral AI)

### Implemented Concepts
* @Tool annotation for exposing Java methods as AI tools
* Spring AI ChatClient with tool registration
* AI-driven orchestration of business logic
* Integration with stock market APIs and database services
* Provider-independent tool execution using Spring AI abstractions
* Comparing tool-calling behavior across different LLM providers

Controller: [WalletController](../src/main/java/com/prj/springai/piotrblog/controllers/WalletController.java)

### Learning Outcome

I learned how to expose Spring Boot services as AI tools, enabling LLMs to invoke application logic and external APIs autonomously.
I also gained experience building AI workflows where the model performs multi-step reasoning by calling multiple tools while remaining independent of the underlying AI provider.
