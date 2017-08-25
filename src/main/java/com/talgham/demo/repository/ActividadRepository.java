package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Actividad;

public interface ActividadRepository extends CrudRepository<Actividad, Long> {

	Actividad findById(Long id);
	Iterable<Actividad> findAll();
}
