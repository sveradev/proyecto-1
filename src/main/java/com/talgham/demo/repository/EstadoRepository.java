package com.talgham.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Estado;

public interface EstadoRepository extends CrudRepository<Estado, Long> {

	Estado findById(Long estado);
	List<Estado> findAll();
	Estado findByOrden(Integer orden);
	Estado findByIdAndActivo(Long estado, Boolean  activo);

}