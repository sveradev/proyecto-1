package com.talgham.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Trabajo;
import com.talgham.demo.repository.TrabajoRepository;
@Component
public class TrabajoService {
	
	@Autowired
	private TrabajoRepository trabajoRepository;

	public Trabajo buscarPorId(Long id) {
		return trabajoRepository.findById(id);
	}

	public String crearTrabajo(Trabajo trabajo) {
		trabajoRepository.save(trabajo);
		return Constantes.GUARDADO;
	}

	public Iterable<Trabajo> buscarTrabajos() {
		return trabajoRepository.findAll();
	}
}
