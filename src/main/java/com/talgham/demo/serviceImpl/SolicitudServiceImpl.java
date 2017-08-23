package com.talgham.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.model.Solicitud;
import com.talgham.demo.repository.SolicitudRepository;

@Component
public class SolicitudServiceImpl implements SolicitudService {
	
	@Autowired
	private SolicitudRepository solicitudRepository;

	public Solicitud addSolicitud (String nombre, String titulo, String email, String descripcion, String responsables,Date fechaSolicitudes) {

		Solicitud solicitud = new Solicitud();
		Date fechaSolicitado = new Date(Calendar.getInstance().getTime().getTime());
		String estado = "SOLICITADO";
		
		solicitud.setTitulo(titulo);
		solicitud.setEmail(email);
		solicitud.setNombre(nombre);
		solicitud.setFechaSolicitado(fechaSolicitado);
		solicitud.setEstado(estado);
		solicitud.setDescripcion(descripcion);
		solicitud.setResponsable(responsables);
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
		return solicitudRepository.findByNombreOrTituloOrResponsable(nombre,titulo,responsable);
	}

	public String updateSolicitud(Solicitud mySolicitud) {
		Long id = mySolicitud.getId();
		String estado = mySolicitud.getEstado();
		String nombre = mySolicitud.getNombre();
		String titulo = mySolicitud.getTitulo();
		String email = mySolicitud.getEmail();
		String descripcion = mySolicitud.getDescripcion();
		String responsable = mySolicitud.getResponsable();
		Date fechaSolicitado = mySolicitud.getFechaSolicitado();
		Date fechaModificado = mySolicitud.getFechaModificado();
		Date fechaFinalizado = mySolicitud.getFechaFinalizado();
		
		Solicitud solicitud = this.buscarPorId(id);
		if (!"".equalsIgnoreCase(estado) && solicitud.getEstado() != estado) {
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
		if (!"".equalsIgnoreCase(responsable) && solicitud.getResponsable() != (responsable)) {
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
