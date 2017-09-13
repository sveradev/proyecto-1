package com.talgham.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Programa;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Trabajo;
import com.talgham.demo.repository.ProgramaRepository;
@Component
public class ProgramaService {
	
	@Autowired
	private ProgramaRepository programaRepository;

	public Programa buscarPorId(Long id) {
		return programaRepository.findById(id);
	}

	public String crearPrograma(Programa programa) {
		programa.setActivo(Boolean.TRUE);
		programa.setFechaAlta(new Date());
		programaRepository.save(programa);
		return Constantes.GUARDADO;
	}

	public Iterable<Programa> buscarProgramas() {
		return programaRepository.findAll();
	}
	
	public Iterable<Programa> buscarActivosPorFechaProximo(Date fechaDesde, Date fechaHasta){
		return programaRepository.findByActivoAndFechaProximoBetween(Boolean.TRUE,fechaDesde,fechaHasta);
	}

	public String guardar(Programa programa) {
		programaRepository.save(programa);
		return Constantes.GUARDADO;
	}
}
