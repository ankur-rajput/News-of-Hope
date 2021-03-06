package edu.ggsipu.newsofhope.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "edu.ggsipu.newsofhope.config", "edu.ggsipu.newsofhope.controllers", "edu.ggsipu.newsofhope.service" })
public class Application {

	public static void main(String[] args) throws Exception {

		SpringApplication.run(Application.class, args);

	}
}
