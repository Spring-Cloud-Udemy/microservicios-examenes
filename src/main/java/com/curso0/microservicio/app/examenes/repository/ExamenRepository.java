package com.curso0.microservicio.app.examenes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.curso0.microservicio.commons.examenes.models.entity.Examen;

public interface ExamenRepository extends JpaRepository<Examen,Long>{

	@Query("select e from Examen e where e.nombre like %?1%")
	//van a retornar los examenes cuando el objeto el nombre del examen sea igual a 1 buscando de izquierda a derecha
	
	public List<Examen> findByNombre(String term);
	
}
