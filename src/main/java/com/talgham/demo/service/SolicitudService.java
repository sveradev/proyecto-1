package com.talgham.demo.service;

import java.util.Date;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.model.Solicitud;
import com.talgham.demo.repository.SolicitudRepository;

@Component
public class SolicitudService {
	
	@Autowired // This means to get the bean called solicitudRepository
	private SolicitudRepository solicitudRepository;

	public Solicitud addSolicitud (String nombre, String titulo, String email, String descripcion) {

		Solicitud solicitud = new Solicitud();
		Date fechaSolicitado = new Date(Calendar.getInstance().getTime().getTime());
		String estado = "SOLICITADO";
		
		solicitud.setTitulo(titulo);
		solicitud.setEmail(email);
		solicitud.setNombre(nombre);
		solicitud.setFechaSolicitado(fechaSolicitado);
		solicitud.setEstado(estado);
		solicitud.setDescripcion(descripcion);
		solicitudRepository.save(solicitud);
		return solicitud;
	}

	public Iterable<Solicitud> getAllSolicitudes() {
		return solicitudRepository.findAll();
	}
	
	public Solicitud buscarPorId(Integer id){
		return solicitudRepository.findById(id);
	}

	public String updateSolicitud(Long id, String estado, String nombre, String titulo, String email, String descripcion) {
		Solicitud solicitud = solicitudRepository.findById(id);
		if (solicitud.getEstado() != estado) {
			solicitud.setEstado(estado);
		}
		if (!solicitud.getNombre().equalsIgnoreCase(nombre)) {
			solicitud.setNombre(nombre);
		}
		if (!solicitud.getTitulo().equalsIgnoreCase(titulo)) {
			solicitud.setTitulo(titulo);
		}
		if (!solicitud.getEmail().equalsIgnoreCase(email)) {
			solicitud.setEmail(email);
		}
		if (!solicitud.getDescripcion().equalsIgnoreCase(descripcion)) {
			solicitud.setDescripcion(descripcion);
		}
		solicitud.setFechaModificado(new Date(System.currentTimeMillis()));
		solicitudRepository.save(solicitud);
		return "updated";
	}
}
