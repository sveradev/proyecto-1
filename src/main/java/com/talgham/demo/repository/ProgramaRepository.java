package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Programa;

public interface ProgramaRepository extends CrudRepository<Programa, Long>{
	
	Programa findById(Long id);
	Iterable<Programa> findAll();

}
