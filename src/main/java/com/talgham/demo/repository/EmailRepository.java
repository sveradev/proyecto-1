package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Email;

public interface EmailRepository extends CrudRepository<Email, Long> {

	Email findByActividad(String actividad);

	Email findById(Long id);

	Email findByActividad_ActividadId(Long actividad);
}
