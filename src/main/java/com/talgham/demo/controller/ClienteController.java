package com.talgham.demo.controller;

import java.util.Date;
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
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.ClienteService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	MessageSource messageSource;

	@RequestMapping("/crearCliente")
	public String cliente(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		if(!usuarioSession.isAdmin()) {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("usuario.sin.permisos",new Object[]{},new Locale("")));
			return "mensaje";
		}
		List<Usuario> contadores = (List<Usuario>)usuarioService.buscarPorPerfil_orden(Constantes.PERFIL_CONTADOR);
		if(contadores == null || contadores.isEmpty()){
			model.addAttribute("usuarios", usuarioService.buscarUsuarios());
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.contadores",new Object[]{},new Locale("")));
			model.addAttribute("usuario",usuarioSession);
			return "usuarios";
		}
		List<Usuario> representantes = (List<Usuario>) usuarioService.buscarPorPerfil_orden(Constantes.PERFIL_CLIENTE);
		if(representantes == null || representantes.isEmpty()){
			model.addAttribute("usuarios", usuarioService.buscarUsuarios());
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.representantes",new Object[]{},new Locale("")));
			model.addAttribute("usuario",usuarioSession);
			return "usuarios";
		}
		model.addAttribute("contadores",contadores);
		model.addAttribute("representantes",representantes);
		return "crearCliente";
	}

	@PostMapping(path="/crearCliente")
	public @ResponseBody ModelAndView crearCliente (@RequestParam String nombre,
			@RequestParam String cuit,
			@RequestParam String descripcion,
			@RequestParam Date cierreEjercicio,
			@RequestParam Long representanteId,
			@RequestParam Long contadorId) {

		ModelAndView result = new ModelAndView("clientes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		Cliente cliente = new Cliente();
		cliente.setNombre(nombre);
		cliente.setCuit(cuit);
		cliente.setDescripcion(descripcion);
		cliente.setCierreEjercicio(cierreEjercicio);
		cliente.setRepresentante(usuarioService.buscarPorId(representanteId));
		cliente.setContador(usuarioService.buscarPorId(contadorId));
		cliente.setFechaAlta(new Date());
		if(!Constantes.GUARDADO.equalsIgnoreCase(clienteService.crearCliente(cliente))){
			result.addObject("clientes", clienteService.buscarClientes());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("cliente.no.creado.error",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("clientes", clienteService.buscarClientes());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("cliente.creado.exito",new Object[]{},new Locale("")));
		return result;
	}
	
	@RequestMapping("/clientes")
	public String clientes(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuario);
		model.addAttribute("clientes", clienteService.buscarClientes());
		return "clientes";
	}
}
