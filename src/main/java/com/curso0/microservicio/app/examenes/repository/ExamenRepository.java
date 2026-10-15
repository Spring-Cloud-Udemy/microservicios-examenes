package com.curso0.microservicio.app.examenes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.curso0.microservicio.commons.examenes.models.entity.Examen;

public interface ExamenRepository extends JpaRepository<Examen, Long> {

    @Query("select e from Examen e where upper(e.nombre) like upper(concat('%', ?1, '%'))")
    public List<Examen> findByNombre(String term);

    @Query("select distinct e.id from Pregunta p join p.examen e where p.id in ?1")
    public List<Long> findExamenesIdsConRespuestasByPreguntaIds(Iterable<Long> preguntaIds);

}