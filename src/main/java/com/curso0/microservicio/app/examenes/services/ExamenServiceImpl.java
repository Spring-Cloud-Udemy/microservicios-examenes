package com.curso0.microservicio.app.examenes.services;

import org.springframework.stereotype.Service;

import com.curso0.microservicio.commons.examenes.models.entity.Examen;
import com.curso0.microservicio.app.examenes.repository.ExamenRepository;
import com.curso0.microservicio.app.spring_commons.services.CommonServiceImpl;

@Service  //importante para poder inyectar
public class ExamenServiceImpl extends CommonServiceImpl<Examen,ExamenRepository>  implements ExamenService {



}
