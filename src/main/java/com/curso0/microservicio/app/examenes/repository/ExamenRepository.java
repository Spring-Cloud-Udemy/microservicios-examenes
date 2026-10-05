package com.curso0.microservicio.app.examenes.repository;

import org.springframework.data.repository.CrudRepository;

import com.curso0.microservicio.commons.examenes.models.entity.Examen;

public interface ExamenRepository extends CrudRepository<Examen,Long>{

	
}
