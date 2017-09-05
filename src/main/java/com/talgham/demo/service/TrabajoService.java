package com.talgham.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.model.Trabajo;
import com.talgham.demo.repository.TrabajoRepository;
@Component
public class TrabajoService {
	
	@Autowired
	private TrabajoRepository trabajoRepository;

	public Trabajo buscarPorId(Long id) {
		return trabajoRepository.findById(id);
	}
}
