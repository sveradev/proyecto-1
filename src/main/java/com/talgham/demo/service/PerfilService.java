package com.talgham.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Perfil;
import com.talgham.demo.repository.PerfilRepository;

@Component
public class PerfilService{
	
	@Autowired
	private PerfilRepository perfilRepository;

	public Iterable<Perfil> getAllPerfiles() {
		return perfilRepository.findAll();
	}

	public Perfil buscarPorId(Long id) {
		return perfilRepository.findById(id);
	}

	public Perfil buscarPorOrden(Integer orden) {
		return perfilRepository.findByOrden(orden);
	}

	public String crearPerfil(Perfil perfil) {
		perfilRepository.save(perfil);
		return Constantes.GUARDADO;
	}
}
