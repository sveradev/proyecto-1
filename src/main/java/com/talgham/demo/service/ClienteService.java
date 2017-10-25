package com.talgham.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Cliente;
import com.talgham.demo.repository.ClienteRepository;
@Component
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id);
	}

	public String crearCliente(Cliente cliente) {
		clienteRepository.save(cliente);
		return Constantes.GUARDADO;
	}

	public Iterable<Cliente> buscarClientes() {
		return clienteRepository.findAll();
	}

	public Cliente buscarPorRepresentante(Long usuario_id) {
		return clienteRepository.findByRepresentante_id(usuario_id);
	}

	public Cliente buscarPorContador(Long usuario_id) {
		return clienteRepository.findByContador_id(usuario_id);
	}

	public String guardar(Cliente cliente) {
		clienteRepository.save(cliente);
		return Constantes.GUARDADO;
	}

	public Iterable<Cliente> buscarClientesActivos() {
		return clienteRepository.findByFechaBajaIsNull();
	}
}