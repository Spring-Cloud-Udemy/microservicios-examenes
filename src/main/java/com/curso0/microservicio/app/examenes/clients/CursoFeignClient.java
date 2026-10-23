package com.curso0.microservicio.app.examenes.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.curso0.microservicio.commons.examenes.models.dto.AsignaturaTreeDto;

@FeignClient("microservicios-cursos")
public interface CursoFeignClient {

	@GetMapping("cursos/alumno/{alumnoId}/asignaturas")
	List<AsignaturaTreeDto> obtenerAsignaturasPorAlumno(@PathVariable Long alumnoId);

	@GetMapping("cursos/alumno/{alumnoId}/id")
	Long obtenerCursoIdPorAlumno(@PathVariable Long alumnoId);
}
