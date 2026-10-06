package com.curso0.microservicio.app.examenes.services;

import com.curso0.microservicio.commons.examenes.models.entity.Asignatura;
import com.curso0.microservicio.commons.examenes.models.entity.Examen;

import java.util.List;

import com.curso0.microservicio.app.spring_commons.services.CommonService;

public interface ExamenService extends CommonService<Examen> {
	//metodo para buscar examen por nombre 
	public List<Examen> findByNombre(String term);
	
	//vamos a usar examen service para los metodos de asginatura
	
	public Iterable<Asignatura> findAllAsignaturas();
	
	
	
	
}
