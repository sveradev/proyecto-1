package com.talgham.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Estado;

public interface EstadoRepository extends CrudRepository<Estado, Long> {

	Estado findById(Long id);
	List<Estado> findAll();

}