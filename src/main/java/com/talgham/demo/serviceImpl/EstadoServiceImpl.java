package com.talgham.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.service.EstadoService;
import com.talgham.demo.model.Estado;
import com.talgham.demo.repository.EstadoRepository;

@Component
public class EstadoServiceImpl implements EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;

	public Iterable<Estado> getAllEstados() {
		return estadoRepository.findAll();
	}
}
