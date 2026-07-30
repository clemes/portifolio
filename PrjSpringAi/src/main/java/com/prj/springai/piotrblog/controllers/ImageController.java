package com.prj.springai.piotrblog.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prj.springai.exceptions.ByteImageException;
import com.prj.springai.piotrblog.model.ImageDescription;
import com.prj.springai.piotrblog.model.ItemImage;

import jakarta.transaction.NotSupportedException;

@RestController
@RequestMapping("/images")
public class ImageController {
	private static final Logger log = LogManager.getLogger(ImageController.class);
	
	private final ObjectMapper mapper = new ObjectMapper();

	private final ChatClient chatClient;
    private List<Media> images;
    private List<Media> dynamicImages = new ArrayList<>();
    
    private ImageModel imageModel;
    
    private VectorStore store;
    
	public ImageController(ChatClient.Builder chatClientBuilder,
			Optional<ImageModel> imageModel,
			   @Autowired(required = false) VectorStore store) {
		
		this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
		
		log.warn("ImageModel provided: {}", imageModel);
		imageModel.ifPresent(model -> this.imageModel = model);

        this.store = store;
		
		this.images = List.of(
                Media.builder().id("fruits").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/fruits.png")).build(),
                Media.builder().id("fruits-2").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/fruits-2.png")).build(),
                Media.builder().id("fruits-3").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/fruits-3.png")).build(),
                Media.builder().id("fruits-4").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/fruits-4.png")).build(),
                Media.builder().id("fruits-5").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/fruits-5.png")).build(),
                Media.builder().id("animals").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/animals.png")).build(),
                Media.builder().id("animals-2").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/animals-2.png")).build(),
                Media.builder().id("animals-3").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/animals-3.png")).build(),
                Media.builder().id("animals-4").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/animals-4.png")).build(),
                Media.builder().id("animals-5").mimeType(MimeTypeUtils.IMAGE_PNG).data(readImageFromClassPath("images/animals-5.png")).build()
        );
	}
	
	private Resource readImageFromClassPath(String image) {
		return new ClassPathResource(image);
	}
	
	/**
	 * Here I investigated a different way to open the image before providing it in the Media of the prompt.
	 * This was an attempt to make sure the bytes sent was correctly.
	 * Conclusion: The images were being sent correctly, the AI model that was not able to process multi-images.
	 * 
	 * @param image
	 * @return
	 * @throws ByteImageException
	 */
	private ByteArrayResource readImageFromByteArray(String image) throws ByteImageException {
		ClassPathResource imageResource = new ClassPathResource(image);
		/**
		 * Here I investigated a different way to open the image before providing it in the Media of the prompt.
		 * This was an attempt to make sure the bytes sent was correctly.
		 */
		byte[] imageBytes;
		try {
			imageBytes = imageResource.getInputStream().readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ByteImageException(e);
		}
		return new ByteArrayResource(imageBytes);
	}
	
	@GetMapping("/describe")
	String describe() {
		List<String> nameImages = List.of("fruits.png","fruits-2.png","fruits-3.png","fruits-4.png","fruits-5.png",
				"animals.png","animals-2.png","animals-3.png","animals-4.png","animals-5.png");
		Integer posImage = (int) (Math.random() * this.images.size());
		String nameImage = nameImages.get(posImage);
		log.info("[describe] Image selected: ({}) {}", posImage, nameImage);
		
//		ByteArrayResource testImage1 = readImageFromByteArray("images/fruits-2.png");
//		ByteArrayResource testImage2 = readImageFromByteArray("images/fruits-5.png");
		
		Media media = this.images.get(posImage);

		String strPrompt;
//		strPrompt = "What is in this image?"
//		strPrompt = "Give me the name of all fruits present in the image";
		strPrompt = "Describe the selected picture, indicating if it is an animal or a fruit/vegetable";
		UserMessage userMessage = UserMessage.builder()
		    .text(strPrompt)
		    .media(List.of(media))
		    .build();

		String response = chatClient.prompt(new Prompt(userMessage))
		    .call()
		    .content();
		
		return String.format("Describing image: %s\n*********\n%s", nameImage, response);
	}

	@GetMapping("/context")
	String context() {
		String msg = """
	    How many pictures did I provide?
	    Answer with a single number.
        """;
        log.info(msg);

        UserMessage um = UserMessage.builder().text(msg).media(images).build();

        return this.chatClient.prompt(new Prompt(um))
                .call()
                .content();
	}
    
	@GetMapping(value = "/find/{object}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    byte[] analyze(@PathVariable String object) {
        String msg = """
        Based on the provided list of picture, which one contains %s.
        Return ONLY a single picture.
        Return ONLY the number of the position (no spaces) of the picture in the media list.
        """.formatted(object);
        log.info(msg);

        UserMessage um = UserMessage.builder().text(msg).media(images).build();

        String content = this.chatClient.prompt(new Prompt(um))
        		.options(
        		        OllamaChatOptions.builder()
        		            .numCtx(8192) //or even more: 16384
        		            .build())
                .call()
                .content();

        log.info("[analyze] Content received: '{}'", content);
        
        if(!this.isNumber(content)) {
        	return content.getBytes();
        }
        
        assert content != null;
        return images.get(Integer.parseInt(content.trim())-1).getDataAsByteArray();
    }
	
	private boolean isNumber(String str) {
		if (str == null || str.trim().isEmpty()) {
	        return false;
	    }

	    try {
	    	Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

	@GetMapping("/describe-all")
	String[] describeAll() {
		String msg = """
	    Explain what do you see on each image.
        """;
        log.info(msg);

        List<Media> imagesPrompt = List.copyOf(Stream.concat(images.stream(), dynamicImages.stream()).toList());
        UserMessage um = UserMessage.builder().text(msg).media(imagesPrompt).build();

        return this.chatClient.prompt(new Prompt(um))
                .call()
                .entity(String[].class);
	}

	@GetMapping(value = "/generate/{object}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
	byte[] generate(@PathVariable String object) throws NotSupportedException, IOException {
		validateImageModel();
		
		String url = generateImageURL(object);
		log.info("Generated URL: {}", url);
		
		UrlResource resource = new UrlResource(url);
		
		this.dynamicImages.add(Media.builder()
				.id(UUID.randomUUID().toString())
				.mimeType(MimeTypeUtils.IMAGE_PNG)
				.data(url)
				.build());
		
		return resource.getContentAsByteArray();
	}

	@GetMapping("/load")
	void load() throws JsonProcessingException, NotSupportedException {
		validateStore();
		
		for(Media image : this.images) {

	        String content = generateImageDescription(image);
	        
	        var doc = Document.builder()
                    .id(image.getId())
                    .text(mapper.writeValueAsString(new ImageDescription(image.getId(), content)))
                    .build();
	        
            store.add(List.of(doc));
            log.info("[load] Document added: {}", image.getId());
		}
	}
	
	@GetMapping("/generate-match/{object}")
	List<Document> generageAndMatch(@PathVariable String object) throws IOException, NotSupportedException{
		validateImageModel();
		validateStore();
		
		String url = generateImageURL(object);
		UrlResource resource = new UrlResource(url);
		log.info("Generated URL: {}", url);
		
		Media newImage = new Media(MimeTypeUtils.IMAGE_PNG, resource);
		String newImageDescription = generateImageDescription(newImage);
		
		SearchRequest searchRequest = SearchRequest.builder()
                .query("""
                Find the most similar description to this: %s
                """.formatted(newImageDescription))
                .topK(2)
                .build();
		
		return this.store.similaritySearch(searchRequest);
	}
	
	private String generateImageDescription(Media image) {
		String msg = """
				Explain what do you see on the image.
				Generate a compact description that explains only what is visible.
				""";
		
		UserMessage um = UserMessage.builder()
				.text(msg)
				.media(image)
				.build();

        return this.chatClient.prompt(new Prompt(um))
                .call()
                .content();
	}
	
	private String generateImageURL(String object) {
		String strPrompt = """
				Generate an image with %s.
				""".formatted(object);
		
		ImageOptions imageOptions = ImageOptionsBuilder.builder()
				.height(320)
				.width(320)
				.N(1)
				.responseFormat("url")
				.build();
		ImageResponse imageResponse = imageModel.call(new ImagePrompt(strPrompt, imageOptions));
		
		return imageResponse.getResult().getOutput().getUrl();
	}
	
	private void validateStore() throws NotSupportedException {
		if(store == null) {
			throw new NotSupportedException("VectorStore is not present, not possible to load image descriptions");
		}
	}
	private void validateImageModel() throws NotSupportedException {
		if(this.imageModel == null) {
			throw new NotSupportedException("ImageModel is not present, generate image not available");
		}
	}
	
	/**
	 * Not explained in the tutorial.
	 * Allow us to categorize pictures present in the classpath of the project.
	 * 
	 * @param image - name of the image to be categorised.
	 * @return name and category of the image.
	 */
	@GetMapping("/categorize/{image}")
    List<ItemImage> categorizeImage(@PathVariable String image) {
        Media media = Media.builder()
                .id(image)
                .mimeType(MimeTypeUtils.IMAGE_PNG)
                .data(new ClassPathResource("images/" + image + ".png"))
                .build();

        UserMessage um = UserMessage.builder().text("""
        List all items you see on the image and define their category.
        Return items inside the JSON array in RFC8259 compliant JSON format.
        """).media(media).build();

        return this.chatClient.prompt(new Prompt(um))
                .call()
                .entity(new ParameterizedTypeReference<List<ItemImage>>() {});
    }
}
