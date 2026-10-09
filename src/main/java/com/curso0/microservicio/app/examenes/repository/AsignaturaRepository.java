package com.curso0.microservicio.app.examenes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.curso0.microservicio.commons.examenes.models.entity.Asignatura;

public interface AsignaturaRepository extends JpaRepository<Asignatura,Long>{

}
