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
import com.talgham.demo.model.Trabajo;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.TrabajoService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class TrabajoController {

	@Autowired
	private TrabajoService trabajoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	MessageSource messageSource;

	@RequestMapping("/crearTrabajo")
	public String trabajo(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		if(!usuarioSession.isAdmin()) {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("usuario.sin.permisos",new Object[]{},new Locale("")));
			return "mensaje";
		}
		return "crearTrabajo";
	}

	@PostMapping(path="/crearTrabajo")
	public @ResponseBody ModelAndView crearTrabajo (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String descripcion) {

		ModelAndView result = new ModelAndView("trabajos");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		Trabajo trabajo = new Trabajo();
		trabajo.setNombre(nombre);
		trabajo.setDescripcion(descripcion);
		trabajo.setFechaAlta(new Date());
		if(!Constantes.GUARDADO.equalsIgnoreCase(trabajoService.crearTrabajo(trabajo))){
			result.addObject("trabajos", trabajoService.buscarTrabajos());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("trabajo.no.creado.error",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("trabajos", trabajoService.buscarTrabajos());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("trabajo.creado.exito",new Object[]{},new Locale("")));
		return result;
	}
	
	@RequestMapping("/trabajos")
	public String trabajos(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuario);
		model.addAttribute("trabajos", trabajoService.buscarTrabajos());
		return "trabajos";
	}
}
