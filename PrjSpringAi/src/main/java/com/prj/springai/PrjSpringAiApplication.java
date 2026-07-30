package com.prj.springai;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.prj.springai.config.SpringAiConfig;
import com.prj.springai.dbtest.Product;
import com.prj.springai.dbtest.ProductRepository;
import com.prj.springai.simplechat.SimpleChatService;

/**
 * 
 */
@SpringBootApplication
@Import(SpringAiConfig.class)
public class PrjSpringAiApplication { // implements CommandLineRunner
	
	private ProductRepository productRepo;
	private SimpleChatService aiChat;
	
	public PrjSpringAiApplication(ProductRepository repository, SimpleChatService aiChat) {
		this.aiChat = aiChat;
		this.productRepo = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(PrjSpringAiApplication.class, args);
	}
	
	/**
	 * Run simple command line, to check which beans were created in the spring-boot context.
	 * 
	 * @param ctx
	 * @return
	 */
//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
//			System.out.println("Let's inspect the beans provided by Spring Boot:");
//			String[] beanNames = ctx.getBeanDefinitionNames();
//			Arrays.sort(beanNames);
//			for (String beanName : beanNames) {
//				System.out.println(beanName);
//			}
//		};
//	}

	/**
	 * Implementation of interface CommandLineRunner to run a command line when the SpringBootApplication starts.
	 *  
	 * @param args
	 * @throws Exception
	 */
//	@Override
	public void run(String... args) throws Exception {
//		String promptText = "configuration for springai project";
		
		this.showProducts();
		
		try (Scanner scanner = new Scanner(System.in)) {
//			System.out.printf("\n*** Enter a string: ");
//			String inputPrompt = scanner.nextLine();
			String inputPrompt = readInput(scanner);
			
			while(!"exit".equals(inputPrompt)) {
				String response = aiChat.promptAi(inputPrompt);
				System.out.println(response);
				
//				inputPrompt = scanner.nextLine();
				inputPrompt = readInput(scanner);
			}
		}
	}
	
	public String readInput(Scanner scanner) {
		System.out.printf("\n*** Enter a string: ");
		String input = scanner.nextLine();
		return input;
	}
	
	public void showProducts() {
		List<Product> allProducts = this.productRepo.findAll();
		System.out.println("*** LIST OF PRODUCTS ***");
		allProducts.forEach(product -> printProduct(product));
		System.out.println("*** END LIST ***");
	}

	private void printProduct(Product product) {
		System.out.println(product);
	}

//	public String readInput() {
//        try (Scanner scanner = new Scanner(System.in)) {
//			System.out.printf("\n*** Enter a string: ");
//			String input = scanner.nextLine();
//			return input;
//		}
//    }
}
