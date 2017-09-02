package com.talgham.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Estado;
import com.talgham.demo.repository.EstadoRepository;

@Component
public class EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;

	public Iterable<Estado> getAllEstados() {
		return estadoRepository.findAll();
	}

	public Estado buscarPorId(Long estado) {
		return estadoRepository.findById(estado);
	}

	public String crearEstado(Estado estado) {
		estadoRepository.save(estado);
		return Constantes.GUARDADO;
	}

	public Estado buscarPorOrden(Integer orden) {
		return estadoRepository.findByOrden(orden);
	}
}
