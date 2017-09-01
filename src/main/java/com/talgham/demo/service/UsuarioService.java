package com.talgham.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Rol;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.repository.UsuarioRepository;

@Component
public class UsuarioService {
	
	@Autowired 
	private UsuarioRepository usuarioRepository;

	public String crearUsuario (Usuario usuario) {
		usuario.setFechaAlta(new Date());		
		usuarioRepository.save(usuario);
		return Constantes.GUARDADO;
	}
	
	public Usuario buscarPorId(Long id){
		return usuarioRepository.findById(id);
	}
	
	public Usuario buscarPorEmail(String email){
		return usuarioRepository.findByEmail(email);
	}

	public Iterable<Usuario> buscarUsuarios() {
		return usuarioRepository.findAll();
	}

	public String updateUsuario(Usuario myUsuario) {
		Long id = myUsuario.getId();
		String nombre = myUsuario.getNombre();
		String alias = myUsuario.getAlias();
		String email = myUsuario.getEmail();
		Rol rol = myUsuario.getRol();
		String password = myUsuario.getPassword();
		Date fechaBaja = myUsuario.getFechaBaja();

		Usuario usuario = usuarioRepository.findById(id);
		if (!"".equalsIgnoreCase(nombre) && !usuario.getNombre().equalsIgnoreCase(nombre)) {
			usuario.setNombre(nombre);
		}
		if (!"".equalsIgnoreCase(alias) && !usuario.getAlias().equalsIgnoreCase(alias)) {
			usuario.setAlias(alias);
		}
		if (!"".equalsIgnoreCase(email) && !usuario.getEmail().equalsIgnoreCase(email)) {
			usuario.setEmail(email);
		}
		if (rol != null && usuario.getRol().equals(rol.getId())) {
			usuario.setRol(rol);
		}
		if (!"".equalsIgnoreCase(password) && usuario.getPassword() != (password)) {
			usuario.setPassword(password);
		}
		if (fechaBaja != null && usuario.getFechaBaja() != null) {
			usuario.setFechaBaja(fechaBaja);
		}
		usuarioRepository.save(usuario);
		return Constantes.GUARDADO;
	}

	public Iterable<Usuario> buscarPorRol(Long id) {
		return usuarioRepository.findByRol(id);
	}
}
