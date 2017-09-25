package com.talgham.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
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
import com.talgham.demo.model.Cliente;
import com.talgham.demo.model.Programa;
import com.talgham.demo.model.Trabajo;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.ClienteService;
import com.talgham.demo.service.ProgramaService;
import com.talgham.demo.service.SolicitudService;
import com.talgham.demo.service.TrabajoService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class ProgramaController {

	@Autowired
	private ProgramaService programaService;
	@Autowired
	private TrabajoService trabajoService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private SolicitudService solicitudService;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	
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
		List<Trabajo> trabajos = (List<Trabajo>) trabajoService.buscarTrabajos();
		if(trabajos == null || trabajos.isEmpty()){
			model.addAttribute("programas", programaService.buscarProgramas());
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.trabajos",new Object[]{},new Locale("")));
			model.addAttribute("usuario",usuarioSession);
			return "programas";
		}
		List<Cliente> clientes = (List<Cliente>) clienteService.buscarClientes();
		if(clientes == null || clientes.isEmpty()){
			model.addAttribute("programas", programaService.buscarProgramas());
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.clientes",new Object[]{},new Locale("")));
			model.addAttribute("usuario",usuarioSession);
			return "programas";
		}
		
		model.addAttribute("trabajos",trabajos);
		model.addAttribute("clientes",clientes);
		return "crearPrograma";
	}

	@PostMapping(path="/crearPrograma")
	public @ResponseBody ModelAndView crearPrograma (@RequestParam String nombre,
			@RequestParam String periodicidad,
			@RequestParam String fechaProximo,
			@RequestParam Long trabajo,
			@RequestParam Long cliente) throws Exception {

		ModelAndView result = new ModelAndView("programas");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		Programa programa = new Programa();
		programa.setNombre(nombre);
		programa.setPeriodicidad(periodicidad);
		programa.setFechaProximo(formatter.parse(fechaProximo));
		programa.setTrabajo(trabajoService.buscarPorId(trabajo));
		programa.setCliente(clienteService.buscarPorId(cliente));
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
	
	@RequestMapping(path="/activarPrograma")
	public @ResponseBody ModelAndView activarPrograma(@RequestParam Long id) throws ParseException {
		
		ModelAndView result = new ModelAndView("programas");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		if(!usuarioSession.isAdmin()) {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("accion.sin.permiso",new Object[]{},new Locale("")));
			
			result.addObject("programas", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			return result;
		}
		Programa programa = programaService.buscarPorId(id);
		programa.setActivo(!programa.getActivo());
		
		if(!Constantes.GUARDADO.equalsIgnoreCase(programaService.guardar(programa))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("programa.no.guardada.error",new Object[]{programa.getId()},new Locale("")));
			result.addObject("programas", programaService.buscarProgramas());
			return result;
		}
		result.addObject("programas", programaService.buscarProgramas());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("programa.activo.exito",new Object[]{programa.getId()},new Locale("")));
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
