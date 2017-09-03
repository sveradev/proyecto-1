package com.talgham.demo.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Rol;

public interface RolRepository extends CrudRepository<Rol, Long> {
	
	Rol findById(Long id);
	Rol findByNombre(String nombre);
	Rol findByOrden(Integer orden);
	Collection<Rol> findAll();
}
