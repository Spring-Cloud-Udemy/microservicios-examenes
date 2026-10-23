package com.curso0.microservicio.app.examenes.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curso0.microservicio.app.examenes.clients.CursoFeignClient;
import com.curso0.microservicio.app.examenes.services.ExamenService;
import com.curso0.microservicio.commons.examenes.models.dto.AsignaturaTreeDto;

@RestController
@RequestMapping("/estudiantes/{estudianteId}")
public class EstudianteController {

	private final CursoFeignClient cursoClient;
	private final ExamenService examenService;

	public EstudianteController(CursoFeignClient cursoClient, ExamenService examenService) {
		this.cursoClient = cursoClient;
		this.examenService = examenService;
	}

	@GetMapping("/asignaturas")
	public ResponseEntity<List<AsignaturaTreeDto>> obtenerAsignaturas(@PathVariable Long estudianteId) {
		List<AsignaturaTreeDto> asignaturas = cursoClient.obtenerAsignaturasPorAlumno(estudianteId);
		return ResponseEntity.ok(asignaturas == null ? List.of() : asignaturas);
	}

	@GetMapping("/asignaturas/{asignaturaId}")
	public ResponseEntity<?> obtenerAsignatura(
			@PathVariable Long estudianteId, @PathVariable Long asignaturaId) {
		List<AsignaturaTreeDto> asignaturas = cursoClient.obtenerAsignaturasPorAlumno(estudianteId);
		if (asignaturas != null) {
			for (AsignaturaTreeDto padre : asignaturas) {
				for (AsignaturaTreeDto hija : padre.getHijos()) {
					if (hija.getId().equals(asignaturaId)) {
						return ResponseEntity.ok(hija);
					}
				}
			}
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("message", "La asignatura no está vinculada al curso del estudiante."));
	}

	@GetMapping("/examenes")
	public ResponseEntity<?> obtenerExamenes(
			@PathVariable Long estudianteId,
			@RequestParam(required = false) Long asignaturaId) {
		Long cursoId = cursoClient.obtenerCursoIdPorAlumno(estudianteId);
		if (cursoId == null) {
			return ResponseEntity.ok(List.of());
		}
		if (asignaturaId != null && !examenService.isAsignaturaVinculada(cursoId, asignaturaId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("message", "La asignatura no está vinculada al curso del estudiante."));
		}
		return ResponseEntity.ok(examenService.findByCursoIdAndAsignaturaId(cursoId, asignaturaId));
	}

	@GetMapping("/examenes/{examenId}")
	public ResponseEntity<?> obtenerExamen(
			@PathVariable Long estudianteId, @PathVariable Long examenId) {
		Long cursoId = cursoClient.obtenerCursoIdPorAlumno(estudianteId);
		if (cursoId == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("message", "El estudiante no tiene un curso asignado."));
		}
		return examenService.findById(examenId)
				.<ResponseEntity<?>>map(examen -> examen.getAsignatura() != null
						&& examenService.isAsignaturaVinculada(cursoId, examen.getAsignatura().getId())
						? ResponseEntity.ok(examen)
						: ResponseEntity.status(HttpStatus.FORBIDDEN).body(
								Map.of("message", "El examen no pertenece a una asignatura de su curso.")))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
