package com.talgham.demo.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Solicitud;

public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {

	Solicitud findById(Long id);
	
//	Iterable<Solicitud> findByNombre(String nombre); 
//	Iterable<Solicitud> findByTitulo(String titulo);
//	Iterable<Solicitud> findByResponsable_usuario_id(Long usuarioId);
//	Iterable<Solicitud> findByNombreAndTitulo(String nombre, String titulo);
//	Iterable<Solicitud> findByNombreAndResponsable_usuario_id(String nombre, Long usuarioId);
//	Iterable<Solicitud> findByTituloAndResponsable_usuario_id(String titulo, Long usuarioId);
//	Iterable<Solicitud> findByNombreAndTituloAndResponsable_usuario_id(String nombre,String titulo, Long responsable);
//	
//	Iterable<Solicitud> findByFechaSolicitadoBetween (Date desde, Date hasta);
	
}
