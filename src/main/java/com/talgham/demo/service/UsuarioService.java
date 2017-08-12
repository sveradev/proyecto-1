package com.talgham.demo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.model.Usuario;
import com.talgham.demo.repository.UsuarioRepository;

@Component
public class UsuarioService {
	
	@Autowired 
	private UsuarioRepository usuarioRepository;

	public String createUsuario (String nombre,String alias,String email, String password) {

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
	
	public Usuario getUsuario(String alias){
		ArrayList<Usuario> usuarios = (ArrayList<Usuario>) this.getAllUsuarios();
		
		if(usuarios != null && !usuarios.isEmpty()){
			for(Usuario usuario: usuarios){
				if(usuario.getAlias() == alias){
					return usuario;
				}
			}
		}
		return null;
	}

	public Iterable<Usuario> getAllUsuarios() {
		return usuarioRepository.findAll();
	}
}