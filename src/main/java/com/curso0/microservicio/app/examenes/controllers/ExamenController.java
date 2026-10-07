package com.curso0.microservicio.app.examenes.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


import com.curso0.microservicio.commons.examenes.models.entity.Examen;

import jakarta.validation.Valid;

import com.curso0.microservicio.app.examenes.services.ExamenService;
import com.curso0.microservicio.app.spring_commons.controllers.CommonController;

@RestController
@RequestMapping("/examenes")
public class ExamenController extends CommonController<Examen,ExamenService>{

	//Creamos el metodo put personalizado para el controller 
	@PutMapping("/{id}")													//El binding result siempre debe de ir despues de la entidad
	public ResponseEntity<?> editar(@Valid @RequestBody Examen examen, BindingResult result,@PathVariable Long id ){
		
		//valida primero si aparece algun errores y si lo tiene lo envia al metodo validar que esta en commons controler 
				if(result.hasErrors()) {
				return this.validar(result);
				}
		
		
		
		
		//comparamos el id que nos dieron en el path url  y miramos si es igual al que tenemos 
		Optional<Examen> o = service.findById(id);
		//si el id guardado es diferente al que nos pasaron dar respuesta not found
		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
			
		}
		//si es igual pasamos los datos del a examendb
		Examen examenDb = o.get();
		//le colocamos el nombre del examen que nos enviaron en el json
		examenDb.setNombre(examen.getNombre());
		
		//comparamos las preguntas que nos dieron con las guardadas 
		//metodo para sacar las preguntas que nos envian de la lista y eliminarlas mediante el orphan
		                                   //pdb = la pregunta de la base de datos 
		//le decimos al stream filter que por cada pregunta que sea diferente a la pregunta que tenemos en la base de datos vamos a quitarla
		
		examenDb.getPreguntas()
		.stream()
		.filter(pdb -> !examen.getPreguntas().contains(pdb))
		.forEach(p -> {
			examenDb.removePregunta(p);
		});
		
		//metodo para añadir las preguntas que no estaban, modificarlas 
		examenDb.setPreguntas(examen.getPreguntas());
		//Respuesta http de retorno y guardar la informacion
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(examenDb));
	}
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term ){
		return ResponseEntity.ok(service.findByNombre(term));
	}
	
	//Metodos de controller para hacer AsignaturaController independiente de ExamenService
	
	//listamos todas las asignaturas
	@GetMapping("/asignaturas")
	public ResponseEntity<?> listarAsignaturas(){
		return ResponseEntity.ok(service.findAllAsignaturas());
	}
	
} 
