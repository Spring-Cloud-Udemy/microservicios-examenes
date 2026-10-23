package com.curso0.microservicio.app.examenes.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.curso0.microservicio.commons.examenes.models.entity.Examen;

public interface ExamenRepository extends JpaRepository<Examen, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Examen e SET e.asignatura = null WHERE e.asignatura.id IN :ids")
    int desvincularAsignaturas(@Param("ids") Collection<Long> ids);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            DELETE FROM Pregunta p
            WHERE p.examen.id IN (
                SELECT e.id FROM Examen e WHERE e.asignatura.id IN :ids
            )
            """)
    int deletePreguntasDeAsignaturas(@Param("ids") Collection<Long> ids);

    @Query("select e from Examen e where upper(e.nombre) like upper(concat('%', ?1, '%'))")
    public List<Examen> findByNombre(String term);

    @Query("select distinct e.id from Pregunta p join p.examen e where p.id in ?1")
    public List<Long> findExamenesIdsConRespuestasByPreguntaIds(Iterable<Long> preguntaIds);

    @Query("select e from Pregunta p join p.examen e where p.id = ?1")
    Optional<Examen> findExamenByPreguntaId(Long preguntaId);

    @Query(value = """
            SELECT e.*
            FROM examenes e
            JOIN curso_asignatura ca ON ca.asignatura_id = e.asignatura_id
            WHERE ca.curso_id = :cursoId
              AND (:asignaturaId IS NULL OR ca.asignatura_id = :asignaturaId)
            """, nativeQuery = true)
    List<Examen> findByCursoIdAndAsignaturaId(
            @Param("cursoId") Long cursoId,
            @Param("asignaturaId") Long asignaturaId);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM curso_asignatura ca
                WHERE ca.curso_id = :cursoId
                  AND ca.asignatura_id = :asignaturaId
            )
            """, nativeQuery = true)
    boolean existsAsignaturaVinculada(
            @Param("cursoId") Long cursoId,
            @Param("asignaturaId") Long asignaturaId);

    @Modifying
    @Query(value = "DELETE FROM curso_asignatura WHERE asignatura_id IN (:ids)", nativeQuery = true)
    int deleteCursoLinksForAsignaturaIds(@Param("ids") Collection<Long> ids);
}