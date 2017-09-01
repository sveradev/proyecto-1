package com.talgham.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.repository.SolicitudRepository;

@Component
public class SolicitudService {
	
	@Autowired
	private SolicitudRepository solicitudRepository;
	@Autowired
	private EstadoService estadoService;

	public String addSolicitud (Solicitud solicitud) {
		if(solicitud.getFechaSolicitado() == null){
			solicitud.setFechaSolicitado(new Date());
		}
		solicitud.setEstado(estadoService.buscarPorId(Constantes.ESTADO_SOLICITADO));
		solicitudRepository.save(solicitud);
		return Constantes.GUARDADO;
	}

	public Iterable<Solicitud> getAllSolicitudes() {
		return solicitudRepository.findAll();
	}
	
	public Solicitud buscarPorId(Long id){
		return solicitudRepository.findById(id);
	}

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
		if (responsable != null && solicitud.getResponsable() != (responsable)) {
			solicitud.setResponsable(responsable);
		}
		if (fechaSolicitado != null && solicitud.getFechaSolicitado().compareTo(fechaSolicitado) == 0) {
			solicitud.setFechaSolicitado(fechaSolicitado);
		}
		solicitud.setFechaModificado(new Date());
		if(estado!= null && estado.getOrden() == Constantes.ESTADO_FINALIZADO){
			solicitud.setFechaFinalizado(new Date());
		}
		solicitudRepository.save(solicitud);
		return Constantes.GUARDADO;
	}

	public Iterable <Solicitud> buscarPorFechas(Date desde, Date hasta) {
		return solicitudRepository.findByFechaSolicitadoBetween(desde, hasta);
	}

	public Iterable <Solicitud> buscar(String nombre, String titulo, Long responsable, Date desde, Date hasta) {
		if(desde != null && hasta != null){
			if(nombre == null || nombre.trim().equalsIgnoreCase("")){
				if(titulo == null || titulo.trim().equalsIgnoreCase("")){
					if(responsable == null){
						return solicitudRepository.findByFechaSolicitadoBetween(desde, hasta);
					} else {
						return solicitudRepository.findByResponsable_idAndFechaSolicitadoBetween(responsable, desde, hasta);
					}
				} else {
					if(responsable == null){
						return solicitudRepository.findByTituloAndFechaSolicitadoBetween(titulo, desde, hasta);
					} else {
						return solicitudRepository.findByTituloAndResponsable_idAndFechaSolicitadoBetween(titulo, responsable, desde, hasta);
					}
				}
			} else {
				if(titulo == null || titulo.trim().equalsIgnoreCase("")){
					if(responsable == null){
						return solicitudRepository.findByNombreAndFechaSolicitadoBetween(nombre, desde, hasta);
					} else {
						return solicitudRepository.findByNombreAndResponsable_idAndFechaSolicitadoBetween(nombre, responsable, desde, hasta);
					}
				} else {
					if(responsable == null){
						return solicitudRepository.findByNombreAndTituloAndFechaSolicitadoBetween(nombre, titulo, desde, hasta);
					} else {
						return solicitudRepository.findByNombreAndTituloAndResponsable_idAndFechaSolicitadoBetween(nombre,titulo, responsable, desde, hasta);
					}
				}
			}
		} else {
			return null;
		}
	}
}
