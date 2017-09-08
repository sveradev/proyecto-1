package com.talgham.demo.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Solicitud;

public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {

	Solicitud findById(Long id);
	
	Iterable<Solicitud> findByNombre(String nombre); 
	Iterable<Solicitud> findByFechaSolicitadoBetween (Date desde, Date hasta);
	Iterable<Solicitud> findByNombreAndFechaSolicitadoBetween (String nombre, Date desde, Date hasta);
	
}
