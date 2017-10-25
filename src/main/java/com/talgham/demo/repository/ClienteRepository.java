package com.talgham.demo.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

	Cliente findById(Long id);
	Iterable<Cliente> findAll();
	Cliente findByRepresentante_id(Long usuario_id);
	Cliente findByContador_id(Long usuario_id);
	Cliente findByIdAndFechaBaja(Long id, Date fechaBaja);
	Iterable<Cliente> findByFechaBajaNotNull();
	Iterable<Cliente> findByFechaBajaIsNull();
}
