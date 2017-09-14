package com.talgham.demo.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Solicitud;

public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {

	Solicitud findById(Long id);
	
	Iterable<Solicitud> findByFechaSolicitadoBetween (Date desde, Date hasta);
	Iterable<Solicitud> findByClienteAndProgramada(Long id,Boolean programada);

	Iterable<Solicitud> findByProgramada(Boolean programada);
	
}
