package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Solicitud;

public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {

	Solicitud findById(Long id);

}
