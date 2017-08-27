package com.talgham.demo.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.repository.EstadoRepository;
import com.talgham.demo.repository.SolicitudRepository;

@Component
public class SolicitudService {
	
	@Autowired
	private SolicitudRepository solicitudRepository;
	@Autowired
	EstadoRepository estadoRepository;
	
	public Solicitud addSolicitud (Solicitud solicitud) {

		if(solicitud.getFechaSolicitado() != null){
			solicitud.setFechaSolicitado(new Date());
		}
		Estado estado = estadoRepository.findByOrden(Constantes.ESTADO_SOLICITADO);
		solicitud.setEstado(estado);
		solicitudRepository.save(solicitud);
		return solicitud;
	}

	public Iterable<Solicitud> getAllSolicitudes() {
		return solicitudRepository.findAll();
	}
	
	public Solicitud buscarPorId(Long id){
		return solicitudRepository.findById(id);
	}
	
	public Iterable<Solicitud> buscarPorCampos(String nombre, String titulo, String responsable) {
		Iterable<Solicitud> solicitudes = null;
		if(nombre != null && !nombre.trim().equalsIgnoreCase("")){
			if(titulo != null && !titulo.trim().equalsIgnoreCase("")){
				if(responsable != null && !responsable.trim().equalsIgnoreCase("")){
					solicitudes = solicitudRepository.findByNombreAndTituloAndResponsable(nombre,titulo,responsable);
				} else {
					solicitudes = solicitudRepository.findByNombreAndTitulo(nombre,titulo);
				}
			} else {
				if(responsable != null && !responsable.trim().equalsIgnoreCase("")){
					solicitudes = solicitudRepository.findByNombreAndResponsable(nombre,responsable);
				} else {
					solicitudes = solicitudRepository.findByNombre(nombre);
				}
			}	
		} else {
			if(titulo != null && !titulo.trim().equalsIgnoreCase("")){
				if(responsable != null && !responsable.trim().equalsIgnoreCase("")){
					solicitudes = solicitudRepository.findByTituloAndResponsable(titulo,responsable);
				} else {
					solicitudes = solicitudRepository.findByTitulo(titulo);
				}
			} else {
				if(responsable != null && !responsable.trim().equalsIgnoreCase("")){
					solicitudes = solicitudRepository.findByResponsable(responsable);
				}
			}
		}
		return solicitudes;
	}
	
//	public Iterable<Solicitud> buscarPorFechaSolicitudEntre (Date desde, Date hasta){
//		return solicitudRepository.findByfechaSolicitudBetween(desde, hasta);
//	}

	public String updateSolicitud(Solicitud mySolicitud) {
		Long id = mySolicitud.getId();
		Estado estado = mySolicitud.getEstado();
		String nombre = mySolicitud.getNombre();
		String titulo = mySolicitud.getTitulo();
		String email = mySolicitud.getEmail();
		String descripcion = mySolicitud.getDescripcion();
		Usuario responsable = mySolicitud.getResponsable();
		Date fechaSolicitado = mySolicitud.getFechaSolicitado();
		Date fechaModificado = mySolicitud.getFechaModificado();
		Date fechaFinalizado = mySolicitud.getFechaFinalizado();
		
		Solicitud solicitud = this.buscarPorId(id);
		if (estado!= null && solicitud.getEstado() != estado) {
			solicitud.setEstado(estado);
		}
		if (!"".equalsIgnoreCase(nombre) && !solicitud.getNombre().equalsIgnoreCase(nombre)) {
			solicitud.setNombre(nombre);
		}
		if (!"".equalsIgnoreCase(titulo) && !solicitud.getTitulo().equalsIgnoreCase(titulo)) {
			solicitud.setTitulo(titulo);
		}
		if (!"".equalsIgnoreCase(email) && !solicitud.getEmail().equalsIgnoreCase(email)) {
			solicitud.setEmail(email);
		}
		if (!"".equalsIgnoreCase(descripcion) && !solicitud.getDescripcion().equalsIgnoreCase(descripcion)) {
			solicitud.setDescripcion(descripcion);
		}
		if (responsable!= null && solicitud.getResponsable() != (responsable)) {
			solicitud.setResponsable(responsable);
		}
		if (fechaSolicitado != null && solicitud.getFechaSolicitado().compareTo(fechaSolicitado) == 0) {
			solicitud.setFechaSolicitado(fechaSolicitado);
		}
		if (fechaModificado != null) {
			solicitud.setFechaModificado(fechaModificado);
		}
		if (fechaFinalizado != null) {
			solicitud.setFechaFinalizado(fechaFinalizado);
		}
		solicitud.setFechaModificado(new Date(System.currentTimeMillis()));
		solicitudRepository.save(solicitud);
		return "updated";
	}
}
