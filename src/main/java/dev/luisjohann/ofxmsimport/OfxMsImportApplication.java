package dev.luisjohann.ofxmsimport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OfxMsImportApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfxMsImportApplication.class, args);
	}

	// @Bean
	// public WebClient webClient() {
	// return WebClient.builder()
	// // .filter(new ServletBearerExchangeFilterFunction())
	// .build();
	// }

}
