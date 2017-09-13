package com.talgham.demo.controller;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Perfil;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.PerfilService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class PerfilController {

	@Autowired
	private PerfilService perfilService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	MessageSource messageSource;
	
	@RequestMapping("/crearPerfil")
	public String perfil(Model model) {
		return "crearPerfil";
	}

	@PostMapping(path="/crearPerfil")
	public @ResponseBody ModelAndView crearPerfil (@RequestParam String nombre,
			@RequestParam Integer orden,
			@RequestParam String descripcion) {

		ModelAndView result = new ModelAndView("perfiles");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		Perfil perfil = new Perfil();
		perfil.setNombre(nombre);
		perfil.setOrden(orden);
		perfil.setDescripcion(descripcion);
		perfil.setFechaAlta(new Date());

		if(!Constantes.GUARDADO.equalsIgnoreCase(perfilService.crearPerfil(perfil))){
			result.addObject("perfiles", perfilService.getAllPerfiles());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("perfil.no.creado.error",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("perfiles", perfilService.getAllPerfiles());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("perfil.creado.exito",new Object[]{},new Locale("")));
		return result;
	}
	
	@RequestMapping("/perfiles")
	public String perfiles(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuario);
		model.addAttribute("perfiles", perfilService.getAllPerfiles());
		return "perfiles";
	}
}
