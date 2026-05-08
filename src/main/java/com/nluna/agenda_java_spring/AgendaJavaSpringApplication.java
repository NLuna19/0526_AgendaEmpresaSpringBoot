package com.nluna.agenda_java_spring;

import com.nluna.agenda_java_spring.service.ICiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class AgendaJavaSpringApplication implements CommandLineRunner {
	String nl = System.lineSeparator();

	@Autowired
	private ICiudadService ciudadService;

	public static void main(String[] args) {
		log.info("App init");
		SpringApplication.run(AgendaJavaSpringApplication.class, args);
		log.info("App finish");
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("** AGENDA EMPRESA APP");
	}
}
