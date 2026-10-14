package com.curso0.microservicio.app.examenes.services;

import com.curso0.microservicio.commons.examenes.models.entity.Asignatura;
import com.curso0.microservicio.commons.examenes.models.entity.Examen;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.curso0.microservicio.app.spring_commons.services.CommonService;

public interface ExamenService extends CommonService<Examen> {
	//metodo para buscar examen por nombre 
	public List<Examen> findByNombre(String term);
	
	//vamos a usar examen service para los metodos de asginatura
	
	public Iterable<Asignatura> findAllAsignaturas();
	
	@Query("select e.id from Pregunta p  join fetch r.pregunta p join fetch p.examen e where r.alumnoId=?1 and e.id=?2")
	public Iterable<Long> findExamenesIdsConRespuestasByPreguntaIds(Iterable<Long> preguntaIds);
	
	
}
