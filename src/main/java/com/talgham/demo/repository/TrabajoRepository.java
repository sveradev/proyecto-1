package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Trabajo;

public interface TrabajoRepository extends CrudRepository<Estado, Long>{
	
	public Trabajo findById(Long id);

}
