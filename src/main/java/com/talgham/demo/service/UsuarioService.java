package com.talgham.demo.service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.model.Usuario;
import com.talgham.demo.repository.UsuarioRepository;

@Component
public class UsuarioService {
	
	@Autowired 
	private UsuarioRepository usuarioRepository;

	public String crearUsuario (String nombre,String alias,String email, String password) {

		Usuario n = new Usuario();
		Date fechaAlta = new Date(Calendar.getInstance().getTime().getTime());
		
		n.setNombre(nombre);
		n.setFechaAlta(fechaAlta);
		n.setPassword(password);
		n.setEmail(email);
		n.setAlias(alias);
		usuarioRepository.save(n);
		return "Saved";
	}
	
	public Usuario buscarUsuarioPorEmail(String email){
		return usuarioRepository.findByEmail(email);
	}

	public Iterable<Usuario> buscarUsuarios() {
		return usuarioRepository.findAll();
	}

	public List<Usuario> buscarUsuariosPorRol(Long rol) {
		List<Usuario> users = usuarioRepository.findByRol(rol);
		return usuarioRepository.findByRol(rol);
	}
}