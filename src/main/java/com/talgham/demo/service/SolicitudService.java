package com.talgham.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Cliente;
import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Trabajo;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.repository.ClienteRepository;
import com.talgham.demo.repository.SolicitudRepository;

@Component
public class SolicitudService {
	
	@Autowired
	private SolicitudRepository solicitudRepository;
	@Autowired
	private EstadoService estadoService;
	@Autowired
	private ClienteRepository clienteRepository;
	
	public String addSolicitud (Solicitud solicitud) {
		if(solicitud.getFechaSolicitado() == null){
			solicitud.setFechaSolicitado(new Date());
		}
		solicitud.setEstado(estadoService.buscarPorOrden(Constantes.ESTADO_SOLICITADO));
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
		Trabajo trabajo = mySolicitud.getTrabajo();
		String descripcion = mySolicitud.getDescripcion();
		Date fechaSolicitado = mySolicitud.getFechaSolicitado();
		
		Solicitud solicitud = this.buscarPorId(id);
		if (estado!= null && solicitud.getEstado() != estado) {
			solicitud.setEstado(estado);
		}
		if (!"".equalsIgnoreCase(nombre) && !solicitud.getNombre().equalsIgnoreCase(nombre)) {
			solicitud.setNombre(nombre);
		}
		if (trabajo != null && solicitud.getTrabajo().getId() != trabajo.getId()) {
			solicitud.setTrabajo(trabajo);
		}
		if (!"".equalsIgnoreCase(descripcion) && !solicitud.getDescripcion().equalsIgnoreCase(descripcion)) {
			solicitud.setDescripcion(descripcion);
		}
		if (fechaSolicitado != null && solicitud.getFechaSolicitado().compareTo(fechaSolicitado) != 0) {
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

	public Iterable <Solicitud> buscar(String nombre, Date desde, Date hasta) {
		if(desde != null && hasta != null){
			if(nombre == null || nombre.trim().equalsIgnoreCase("")){
				return solicitudRepository.findByFechaSolicitadoBetween(desde, hasta);
			} 
			return solicitudRepository.findByNombreAndFechaSolicitadoBetween(nombre, desde, hasta);
		}
		return null;
	}

	public Iterable<Solicitud> buscarSolicitudes(Usuario usuario) {
		Cliente cliente = clienteRepository.findById(usuario.getId());
		return solicitudRepository.findByClienteAndProgramada(cliente.getId(), Boolean.FALSE);
	}

	public Iterable<Solicitud> buscarAgenda(Usuario usuario) {
		Cliente cliente = clienteRepository.findById(usuario.getId());
		return solicitudRepository.findByClienteAndProgramada(cliente.getId(), Boolean.TRUE);
	}
}
