package com.talgham.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Perfil;
import com.talgham.demo.model.Rol;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.repository.UsuarioRepository;

@Component
public class UsuarioService {
	
	@Autowired 
	private UsuarioRepository usuarioRepository;

	public String crearUsuario (Usuario usuario) {
		usuario.setFechaAlta(new Date());
		String password = usuario.getPassword();
		
		String salt = BCrypt.gensalt(12);
		String hashPassword = BCrypt.hashpw(password, salt);
		usuario.setPassword(hashPassword);
		
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
		Perfil perfil = myUsuario.getPerfil();
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
		if (rol != null && usuario.getRol().equals(rol)) {
			usuario.setRol(rol);
		}
		if (perfil != null && usuario.getPerfil().equals(perfil)) {
			usuario.setPerfil(perfil);
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
		return usuarioRepository.findByRol_id(id);
	}

	public Iterable<Usuario> buscarPorPerfil(Long id) {
		return usuarioRepository.findByPerfil_id(id);
	}

	public Iterable<Usuario> buscarPorPerfil_orden(Integer orden) {
		return usuarioRepository.findByPerfil_orden(orden);
	}
}
