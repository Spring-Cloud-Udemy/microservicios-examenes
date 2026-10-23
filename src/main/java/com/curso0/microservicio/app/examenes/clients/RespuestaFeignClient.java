package com.curso0.microservicio.app.examenes.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("microservicios-respuestas")
public interface RespuestaFeignClient {

	@DeleteMapping("/respuestas/asignatura")
	void eliminarPorAsignaturas(@RequestParam("asignaturaIds") List<Long> asignaturaIds);
}
