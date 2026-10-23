package com.curso0.microservicio.app.examenes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.curso0.microservicio.app.examenes.clients.RespuestaFeignClient;
import com.curso0.microservicio.app.examenes.repository.AsignaturaRepository;
import com.curso0.microservicio.app.examenes.repository.ExamenRepository;
import com.curso0.microservicio.app.spring_commons.services.CommonServiceImpl;
import com.curso0.microservicio.commons.examenes.models.entity.Asignatura;
import com.curso0.microservicio.commons.examenes.models.entity.Examen;

@Service  //importante para poder inyectar
public class ExamenServiceImpl extends CommonServiceImpl<Examen,ExamenRepository>  implements ExamenService {

	private final AsignaturaRepository asignaturaRepository;
	private final RespuestaFeignClient respuestaClient;

	public ExamenServiceImpl(AsignaturaRepository asignaturaRepository, RespuestaFeignClient respuestaClient) {
		this.asignaturaRepository = asignaturaRepository;
		this.respuestaClient = respuestaClient;
	}
	
	@Override
	@Transactional(readOnly = true )
	public List<Examen> findByNombre(String term) {
		// TODO Auto-generated method stub
		return repository.findByNombre(term);
	}

	@Override
	@Transactional(readOnly = true)
	//me permite recorrer los datos que retornamos
	public Iterable<Asignatura> findAllAsignaturas() {
		return asignaturaRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Asignatura> findAsignaturaById(Long id) {
		return asignaturaRepository.findById(id);
	}

	@Override
	@Transactional
	public Examen save(Examen examen) {
		Asignatura asignaturaRequest = examen.getAsignatura();
		if (asignaturaRequest == null || asignaturaRequest.getId() == null) {
			throw new IllegalArgumentException(
					"El examen debe estar vinculado a una asignatura hija existente.");
		}
		Asignatura asignatura = asignaturaRepository.findById(asignaturaRequest.getId())
				.orElseThrow(() -> new IllegalArgumentException(
						"El examen debe estar vinculado a una asignatura hija existente."));
		if (asignatura.getPadre() == null) {
			throw new IllegalArgumentException("El examen solo puede vincularse a una asignatura hija.");
		}
		examen.setAsignatura(asignatura);
		return super.save(examen);
	}

	@Override
	@Transactional
	public Asignatura saveAsignatura(Asignatura asignatura) {
		if (asignatura.getPadre() != null && asignatura.getPadre().getId() != null) {
			Long padreId = asignatura.getPadre().getId();
			if (asignatura.getId() != null && padreId.equals(asignatura.getId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Una asignatura no puede ser padre de sí misma.");
			}
			Asignatura padre = asignaturaRepository.findById(padreId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"Asignatura padre no encontrada."));
			assertNoCiclo(asignatura.getId(), padre);
			asignatura.setPadre(padre);
		} else {
			asignatura.setPadre(null);
		}
		return asignaturaRepository.save(asignatura);
	}

	private void assertNoCiclo(Long asignaturaId, Asignatura padre) {
		Asignatura actual = padre;
		while (actual != null) {
			if (asignaturaId != null && asignaturaId.equals(actual.getId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"No se puede crear una relación circular entre asignaturas.");
			}
			actual = actual.getPadre();
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<Long> findExamenesIdsConRespuestasByPreguntaIds(Iterable<Long> preguntaIds) {
		// TODO Auto-generated method stub
		return repository.findExamenesIdsConRespuestasByPreguntaIds(preguntaIds);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Examen> findExamenByPreguntaId(Long preguntaId) {
		return repository.findExamenByPreguntaId(preguntaId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Examen> findByCursoIdAndAsignaturaId(Long cursoId, Long asignaturaId) {
		return repository.findByCursoIdAndAsignaturaId(cursoId, asignaturaId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isAsignaturaVinculada(Long cursoId, Long asignaturaId) {
		return repository.existsAsignaturaVinculada(cursoId, asignaturaId);
	}

	@Override
	@Transactional
	public void deleteAsignatura(Long asignaturaId) {
		Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
				.orElseThrow(() -> new NoSuchElementException("Asignatura no encontrada."));
		List<Long> asignaturaIds = new ArrayList<>();
		asignaturaIds.add(asignatura.getId());
		if (asignatura.getPadre() == null) {
			for (Asignatura hija : asignatura.getHijos()) {
				asignaturaIds.add(hija.getId());
			}
		}
		repository.deletePreguntasDeAsignaturas(asignaturaIds);
		repository.desvincularAsignaturas(asignaturaIds);
		repository.deleteCursoLinksForAsignaturaIds(asignaturaIds);
		asignaturaRepository.delete(asignatura);
		asignaturaRepository.flush();
		respuestaClient.eliminarPorAsignaturas(asignaturaIds);
	}

}
