package com.talgham.demo.service;

import java.util.Calendar;
import java.util.Date;
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
	
	public Usuario buscarUsuarioPorId(Long id){
		return usuarioRepository.findById(id);
	}
	
	public Usuario buscarUsuarioPorEmail(String email){
		return usuarioRepository.findByEmail(email);
	}

	public Iterable<Usuario> buscarUsuarios() {
		return usuarioRepository.findAll();
	}

	public List<Usuario> buscarUsuariosPorRol(String rol) {
		return usuarioRepository.findByRol(rol);
	}

	public String updateUsuario(Usuario myUsuario) {
		Long id = myUsuario.getId();
		String nombre = myUsuario.getNombre();
		String alias = myUsuario.getAlias();
		String email = myUsuario.getEmail();
		String rol = myUsuario.getRol();
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
		if (!"".equalsIgnoreCase(rol) && !usuario.getRol().equalsIgnoreCase(rol)) {
			usuario.setRol(rol);
		}
		if (!"".equalsIgnoreCase(password) && usuario.getPassword() != (password)) {
			usuario.setPassword(password);
		}
		if (fechaBaja != null && usuario.getFechaBaja() != null) {
			usuario.setFechaBaja(fechaBaja);
		}
		usuarioRepository.save(usuario);
		return "guardado";
	}
}
