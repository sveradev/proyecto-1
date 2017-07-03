package com.talgham.demo.service;

import java.sql.Date;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.model.Solicitud;
import com.talgham.demo.repository.SolicitudRepository;

@Component
public class SolicitudService {
	
	@Autowired // This means to get the bean called solicitudRepository
	private SolicitudRepository solicitudRepository;

	public String addSolicitud (String nombre, String titulo, String email, String descripcion) {

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
		return "Saved";
	}

	public Iterable<Solicitud> getAllSolicitudes() {
		return solicitudRepository.findAll();
	}
}