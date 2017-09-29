package com.talgham.demo.controller;

import java.text.SimpleDateFormat;
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
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

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
			@RequestParam String cierreEjercicio,
			@RequestParam Long representanteId,
			@RequestParam Long contadorId) throws Exception {

		ModelAndView result = new ModelAndView("clientes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		Cliente cliente = new Cliente();
		cliente.setNombre(nombre);
		cliente.setCuit(cuit);
		cliente.setDescripcion(descripcion);
		cliente.setCierreEjercicio(formatter.parse(cierreEjercicio));
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
	
	@RequestMapping("/editarCliente")
	public String editarCliente(@RequestParam(value="id") Long id, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		if(usuarioSession.isAdmin()) {
			List<Usuario> contadores = (List<Usuario>)usuarioService.buscarPorPerfil_orden(Constantes.PERFIL_CONTADOR);
			if(contadores == null || contadores.isEmpty()){
				model.addAttribute("clientes", clienteService.buscarClientes());
				model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
				model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.contadores",new Object[]{},new Locale("")));
				model.addAttribute("usuario",usuarioSession);
				return "clientes";
			}
			List<Usuario> representantes = (List<Usuario>) usuarioService.buscarPorPerfil_orden(Constantes.PERFIL_CLIENTE);
			if(representantes == null || representantes.isEmpty()){
				model.addAttribute("clientes", clienteService.buscarClientes());
				model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
				model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.representantes",new Object[]{},new Locale("")));
				model.addAttribute("usuario",usuarioSession);
				return "clientes";
			}
			model.addAttribute("contadores",contadores);
			model.addAttribute("representantes",representantes);
		} else if (usuarioSession.getId() != id){
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("usuario.sin.permisos",new Object[]{},new Locale("")));
			return "mensaje";
		}
		model.addAttribute("cliente", clienteService.buscarPorId(id));
		return "editarCliente";
	}

	@PostMapping(path="/editarCliente")
	public @ResponseBody ModelAndView editarCliente (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String cuit,
			@RequestParam String descripcion,
			@RequestParam String cierreEjercicio,
			@RequestParam Long representanteId,
			@RequestParam Long contadorId) throws Exception {
		
		ModelAndView result = new ModelAndView("clientes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		Cliente cliente = clienteService.buscarPorId(id);
		cliente.setNombre(nombre);
		cliente.setCuit(cuit);
		cliente.setDescripcion(descripcion);
		cliente.setCierreEjercicio(formatter.parse(cierreEjercicio));
		cliente.setRepresentante(usuarioService.buscarPorId(representanteId));
		cliente.setContador(usuarioService.buscarPorId(contadorId));
		if(!Constantes.GUARDADO.equalsIgnoreCase(clienteService.guardar(cliente))){
			result.addObject("clientes", clienteService.buscarClientes());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("cliente.guardado.error",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("clientes", clienteService.buscarClientes());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("cliente.guardado.exito",new Object[]{cliente.getId()},new Locale("")));
		return result;
	}
	
	@RequestMapping("/eliminarCliente")
	public ModelAndView eliminarCliente(@RequestParam(value="id") Long id) {
		ModelAndView result = new ModelAndView("clientes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		Cliente cliente = clienteService.buscarPorId(id);
		if(cliente == null){
			result.addObject("clientes", clienteService.buscarClientes());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("error.cliente.no.encontrado",new Object[]{},new Locale("")));
			return result;
		}
		cliente.setFechaBaja(new Date());
		if(!Constantes.ELIMINADO.equalsIgnoreCase(clienteService.guardar(cliente))){
			result.addObject("clientes", clienteService.buscarClientes());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("cliente.baja.error",new Object[]{cliente.getNombre()},new Locale("")));
			return result;
		}
		result.addObject("clientes", clienteService.buscarClientes());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("cliente.baja.exito",new Object[]{cliente.getNombre()},new Locale("")));
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
