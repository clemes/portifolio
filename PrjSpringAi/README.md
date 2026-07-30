# Multi-Application AI Project with Spring Boot and Spring AI

This project is a collection of AI-powered backend applications developed with Java, Spring Boot, and Spring AI.
The goal of the project was to explore different real-world AI integration scenarios, including text generation, structured AI responses, local LLM execution with Ollama, and AI-assisted financial data analysis.

The applications demonstrate how Large Language Models (LLMs) can be integrated into enterprise-style backend services using clean architecture and REST APIs.

## Technologies Used
* Java
* Spring Boot
* Spring AI
* Ollama
* REST APIs
* Maven
* Log4j
* Large Language Models (LLMs)
* PGVector Database

## Key Learning Outcomes

Through these applications, I gained hands-on experience with:

* Building RESTful APIs with Spring Boot
* Integrating AI models using Spring AI
* Running local LLMs with Ollama
* Creating dynamic prompts (PromptTemplates) and AI workflows
* Mapping AI responses into structured Java objects
* Implementing service-oriented backend architecture
* Logging and monitoring application behavior
* Experimenting with AI-driven business scenarios
* Designing centralized exception handling
* Different AI-Models and their characteristics and features

# Project configuration

There was a couple of blogs and tutorials used to configure the project.
More details can be found in the following links.

* [Steps to configure this project](./docs/config-project.md)
* It was designed a [simple chat](./docs/simple-chat.md) to check if the configuration was in order.

### Device configuration

As an experimental project, it was decided to run the AI Model locally to avoid financial costs during the creation of this experimental project.
**Ollama** provides the necessary infrastructure for that.
A dedicated device was used for that purpose, running the Ollama server and being accessed via the local network.
Following the details of the configuration.

* CPU
* RAM - 16GB
* GPU - Not present.

### Network configuration

### AI Model

The first model selected was _qwen2.5_ based on a personal reference.
Soon it was noticed that this model was too big to run in the configured device.
Then, it was done some exploration on the right model to run based on how strong the configure device is.
Only models lower than 8B-9B in size would be able to run under such lower configuration device.
Experts indicate a minimum of 16GB of GPU to enable the model to work properly and provide a reliable response.
Hence, the conclusion that the device did not have enough power to run an AI model able to handle the basic operations implemented in this project.
Still, it was a good starting point to learn how to integrate AI into a real application.

Below there are the list of models that was experimented:
* qwen2.5:3b
* ministral-3:8b
* gemma3:4b
* llama3.2
* qwen3.5:9b
* mistral

The model below is suitable for image processing
* minicpm-v:8b
* llava:7b

# Application 1 – AI Poetry Generator

Details: [Introduction to Spring AI](./docs/intro-spring-ai-baeldung.md)

# Application 2 – AI Financial Assistant with Ollama

Details: [AI Financial Assistant by Piotr's blog](./docs/use-springai-piotrblog.md)

# Overall Project Outcome

This project strengthened my understanding of modern AI application development in Java.
It allowed me to combine backend engineering practices with generative AI technologies while experimenting with practical AI use cases such as:
* Content generation
* Financial analysis
* AI orchestration
* Semantic search
* Image processing
* Local AI deployment

In addition to learning AI integration, the project also improved my understanding of:

* API architecture
* Exception handling strategies
* Structured response design
* Clean code organization
* Service abstraction patterns
* Enterprise AI integration approaches
* Real-world backend development practices with AI-enabled systems

# Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.13/maven-plugin)
* [SpringAI](https://spring.io/projects/spring-ai)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.13/maven-plugin/build-image.html)
* [Ollama](https://ollama.com/)


