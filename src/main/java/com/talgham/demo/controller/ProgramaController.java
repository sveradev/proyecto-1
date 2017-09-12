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
import com.talgham.demo.model.Programa;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.ProgramaService;
import com.talgham.demo.service.TrabajoService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class ProgramaController {

	@Autowired
	private ProgramaService programaService;
	
	@Autowired
	private TrabajoService trabajoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	MessageSource messageSource;

	@RequestMapping("/crearPrograma")
	public String programa(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		if(!usuarioSession.isAdmin()) {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("usuario.sin.permisos",new Object[]{},new Locale("")));
			return "mensaje";
		}
		return "crearPrograma";
	}

	@PostMapping(path="/crearPrograma")
	public @ResponseBody ModelAndView crearPrograma (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam Date fechaSolicitado,
			@RequestParam Date fechaUltimoCreado,
			@RequestParam Long trabajo_id) {

		ModelAndView result = new ModelAndView("programas");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		Programa programa = new Programa();
		programa.setNombre(nombre);
		programa.setFechaSolicitado(fechaSolicitado);
		programa.setFechaUltimoCreado(fechaUltimoCreado);
		programa.setTrabajo(trabajoService.buscarPorId(trabajo_id));
		if(!Constantes.GUARDADO.equalsIgnoreCase(programaService.crearPrograma(programa))){
			result.addObject("programas", programaService.buscarProgramas());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("programa.no.creado.error",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("programas", programaService.buscarProgramas());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("programa.creado.exito",new Object[]{},new Locale("")));
		return result;
	}
	
	@RequestMapping("/programas")
	public String programas(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuario);
		model.addAttribute("programas", programaService.buscarProgramas());
		return "programas";
	}
}
