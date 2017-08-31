package com.talgham.demo.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Solicitud;

public interface SolicitudRepository extends CrudRepository<Solicitud, Long> {

	Solicitud findById(Long id);
	
	Iterable<Solicitud> findByNombre(String nombre); 
	Iterable<Solicitud> findByTitulo(String titulo);
	Iterable<Solicitud> findByResponsable_id(Long usuarioId);
	Iterable<Solicitud> findByNombreAndTitulo(String nombre, String titulo);
	Iterable<Solicitud> findByNombreAndResponsable_id(String nombre, Long usuarioId);
	Iterable<Solicitud> findByTituloAndResponsable_id(String titulo, Long usuarioId);
	Iterable<Solicitud> findByNombreAndTituloAndResponsable_id(String nombre,String titulo, Long responsable);
	
	Iterable<Solicitud> findByFechaSolicitadoBetween (Date desde, Date hasta);
	Iterable<Solicitud> findByNombreAndFechaSolicitadoBetween (String nombre, Date desde, Date hasta);
	Iterable<Solicitud> findByTituloAndFechaSolicitadoBetween (String titulo, Date desde, Date hasta);
	Iterable<Solicitud> findByResponsable_idAndFechaSolicitadoBetween (Long usuarioId, Date desde, Date hasta);
	Iterable<Solicitud> findByNombreAndTituloAndFechaSolicitadoBetween(String nombre, String titulo, Date desde, Date hasta);
	Iterable<Solicitud> findByTituloAndResponsable_idAndFechaSolicitadoBetween(String titulo, Long usuarioId, Date desde, Date hasta);
	Iterable<Solicitud> findByNombreAndResponsable_idAndFechaSolicitadoBetween(String nombre, Long usuarioId, Date desde, Date hasta);
	Iterable<Solicitud> findByNombreAndTituloAndResponsable_idAndFechaSolicitadoBetween(String nombre,String titulo, Long responsable, Date desde, Date hasta);
	
}
