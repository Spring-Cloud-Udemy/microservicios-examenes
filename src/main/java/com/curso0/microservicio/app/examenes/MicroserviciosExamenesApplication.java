package com.curso0.microservicio.app.examenes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
@EntityScan({"com.curso0.microservicio.commons.examenes.models.entity",
	"com.curso0.microservicio.app.commons.usuarios.models.entity"})

public class MicroserviciosExamenesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosExamenesApplication.class, args);
	}

}
