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
		return estadoRepository.findByActivo(Boolean.TRUE);
	}

	public Estado buscarPorId(Long estado) {
		return estadoRepository.findByIdAndActivo(estado,Boolean.TRUE);
	}

	public String crearEstado(Estado estado) {
		estadoRepository.save(estado);
		return Constantes.GUARDADO;
	}

	public Estado buscarPorOrden(Integer orden) {
		return estadoRepository.findByOrden(orden);
	}
}
