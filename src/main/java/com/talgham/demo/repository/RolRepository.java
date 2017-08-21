package com.talgham.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Rol;

public interface RolRepository extends CrudRepository<Rol, Long> {
	
	Rol findById(Long id);
	Rol findByNombre(String nombre);
	List<Rol> findAll();
}
