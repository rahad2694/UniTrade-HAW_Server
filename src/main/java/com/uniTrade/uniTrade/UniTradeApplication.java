package com.uniTrade.uniTrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class UniTradeApplication {

	public static void main(String[] args) {

		// Dotenv dotenv = Dotenv.load();

		// Set system properties from .env file
		System.setProperty("SPRING_DATA_MONGODB_URI",
				"mongodb+srv://asibul279:uniTrade@unitrade.zjgqj.mongodb.net/?retryWrites=true&w=majority&appName=UniTrade");
		System.setProperty("SPRING_DATA_MONGODB_DATABASE", "UniTrade");

		SpringApplication.run(UniTradeApplication.class, args);
	}

}
