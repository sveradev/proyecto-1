package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Solicitud;

public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {

	Solicitud findById(Long id);
	
	Iterable<Solicitud> findByNombre(String nombre); 
	Iterable<Solicitud> findByTitulo(String titulo);
	Iterable<Solicitud> findByResponsable(String responsable);
	Iterable<Solicitud> findByNombreOrTitulo(String nombre, String titulo,);
	Iterable<Solicitud> findByNombreOrResponsable(String nombre, String responsable);
	Iterable<Solicitud> findByTituloOrResponsable(String titulo, String responsable);
	Iterable<Solicitud> findByNombreOrTituloOrResponsable(String nombre,String titulo, String responsable);

}
