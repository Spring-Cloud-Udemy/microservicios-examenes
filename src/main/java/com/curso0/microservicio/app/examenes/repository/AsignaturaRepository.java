package com.curso0.microservicio.app.examenes.repository;

import org.springframework.data.repository.CrudRepository;

import com.curso0.microservicio.commons.examenes.models.entity.Asignatura;

public interface AsignaturaRepository extends CrudRepository<Asignatura,Long>{

}
