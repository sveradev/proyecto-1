package com.talgham.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talgham.demo.model.Actividad;
import com.talgham.demo.repository.ActividadRepository;

@Service
public class ActividadService {
	
	@Autowired
	private ActividadRepository actividadRepository;
	
	public Iterable<Actividad> getAllActividades() {
		return actividadRepository.findAll();
	}

	public Actividad buscarPorId(Long id) {
		return actividadRepository.findById(id);
	}
}
