package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Email;

public interface EmailRepository extends CrudRepository<Email, Long> {

	Email findById(Long id);

	Email findByActividad_id(Long actividad);
}
