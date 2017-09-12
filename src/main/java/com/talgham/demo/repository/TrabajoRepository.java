package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Trabajo;

public interface TrabajoRepository extends CrudRepository<Trabajo, Long>{
	
	Trabajo findById(Long id);
	Iterable<Trabajo> findAll();

}
