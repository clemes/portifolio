# AI Poetry Generator
* Reference: https://www.baeldung.com/spring-ai

This application generates poems dynamically using AI.
Users can request customized poems by providing a genre and theme through REST endpoints.

## Features
* Generate default AI-created haikus
* Create customized poems based on user input
* Dynamic prompt generation using PromptTemplate
* Structured AI responses mapped to Java records
* Centralized exception handling using @ControllerAdvice

## Technical Highlights

The application is divided into clear layers:

* PoetryController handles REST endpoints and request processing
* PoetryService manages communication with the AI model
* Poem record represents structured response data
* MyGlobalExceptionHandler centralizes error handling across the application

The global exception handler was designed to improve API reliability and user experience by returning meaningful HTTP error responses when communication with the LLM fails.

The [PoetryController](../src/main/java/com/prj/springai/poetry/PoetryController.java) exposes two endpoints:
* one for generating a default haiku about morning coffee,
* and another that accepts user input (JSON) such as genre and theme to create customized poems.

The [PoetryService](../src/main/java/com/prj/springai/poetry/PoetryService.java) handles the interaction with the AI model through ChatClient.
One method calls the ChatClient using a fixed prompt returning the response as plain string.
The second method builds prompts dynamically using PromptTemplate and the genre and theme (user input).
It converts the AI response into a structured Poem object.

The tutorial shows how to define

## Lessons Learned
* Prompt engineering basics
* Using ChatClient in Spring AI
* Building AI-enabled REST APIs
* Handling AI-generated structured data
* Designing centralized exception management