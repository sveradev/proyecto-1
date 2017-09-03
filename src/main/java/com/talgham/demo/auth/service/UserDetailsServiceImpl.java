package com.talgham.demo.auth.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.talgham.demo.controller.SolicitudController;
import com.talgham.demo.model.Rol;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.repository.RolRepository;
import com.talgham.demo.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolesRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByNombre(username);
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(usuario.getRol().getNombre()));

	// FIXME eliminar todo este bloque de prueba
		Collection<Rol> roles = rolesRepository.findAll();
		
		//Ruta completa al template html
		String templateHtml = "src/main/resources/emailTemplates/rolesTest.html";
		
		//Mapa de objetos a renderizar (clave elemento mustache, valor opbejto)
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("roles", roles);
		
		String templateCompilado = SolicitudController.getEmailTemplate(templateHtml, data);
	// fin de pruebs	
		return new User(usuario.getNombre(), usuario.getPassword(), grantedAuthorities);
	}
}
