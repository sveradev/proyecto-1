package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

	Cliente findById(Long id);
	Iterable<Cliente> findAll();
}
