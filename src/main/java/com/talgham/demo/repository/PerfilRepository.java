package com.talgham.demo.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Perfil;

public interface PerfilRepository extends CrudRepository<Perfil, Long> {
	
	Perfil findById(Long id);
	Perfil findByNombre(String nombre);
	Perfil findByOrden(Integer orden);
	Collection<Perfil> findAll();
}
