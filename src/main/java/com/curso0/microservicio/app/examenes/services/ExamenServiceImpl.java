package com.curso0.microservicio.app.examenes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curso0.microservicio.commons.examenes.models.entity.Asignatura;
import com.curso0.microservicio.commons.examenes.models.entity.Examen;
import com.curso0.microservicio.app.examenes.repository.AsignaturaRepository;
import com.curso0.microservicio.app.examenes.repository.ExamenRepository;
import com.curso0.microservicio.app.spring_commons.services.CommonServiceImpl;

@Service  //importante para poder inyectar
public class ExamenServiceImpl extends CommonServiceImpl<Examen,ExamenRepository>  implements ExamenService {

	//inyectamos asignatura repository para utilizarla
	@Autowired
	private AsignaturaRepository asignaturaRepository;
	
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



}
