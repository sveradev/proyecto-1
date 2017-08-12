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

		Solicitud n = new Solicitud();
		Date fechaSolicitado = new Date(Calendar.getInstance().getTime().getTime());
		String estado = "SOLICITADO";
		
		n.setTitulo(titulo);
		n.setEmail(email);
		n.setNombre(nombre);
		n.setFechaSolicitado(fechaSolicitado);
		n.setEstado(estado);
		n.setDescripcion(descripcion);
		solicitudRepository.save(n);
		return n;
	}

	public Iterable<Solicitud> getAllSolicitudes() {
		return solicitudRepository.findAll();
	}

	public String updateSolicitud(Long id, String estado, String nombre, String titulo, String email, String descripcion) {
		Solicitud n = solicitudRepository.findById(id);
		if (n.getEstado() != estado) {
			n.setEstado(estado);
		}
		if (!n.getNombre().equalsIgnoreCase(nombre)) {
			n.setNombre(nombre);
		}
		if (!n.getTitulo().equalsIgnoreCase(titulo)) {
			n.setTitulo(titulo);
		}
		if (!n.getEmail().equalsIgnoreCase(email)) {
			n.setEmail(email);
		}
		if (!n.getDescripcion().equalsIgnoreCase(descripcion)) {
			n.setDescripcion(descripcion);
		}
		n.setFechaRespuesta(new Date(System.currentTimeMillis()));
		solicitudRepository.save(n);
		return "updated";
	}
}