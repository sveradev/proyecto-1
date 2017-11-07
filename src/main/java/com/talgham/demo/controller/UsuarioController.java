package com.talgham.demo.controller;

import java.text.ParseException;
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
import com.talgham.demo.model.Perfil;
import com.talgham.demo.model.Rol;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.ClienteService;
import com.talgham.demo.service.PerfilService;
import com.talgham.demo.service.RolService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private RolService rolService;
	@Autowired
	private PerfilService perfilService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/crearUsuario")
	public String usuario(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		
		if(!usuarioSession.isAdmin()) {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("usuario.sin.permisos",new Object[]{},new Locale("")));
			return "mensaje";
		}
		
		List<Rol> roles = (List<Rol>) rolService.getAllRoles();
		List<Perfil> perfiles = (List<Perfil>) perfilService.getAllPerfiles();
		List<Cliente> clientes = (List<Cliente>) clienteService.buscarClientes();
		if(roles == null || roles.isEmpty()){
			model.addAttribute("usuarios", usuarioService.buscarUsuarios());
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
			model.addAttribute("usuario",usuarioSession);
			return "usuarios";
		}
		if(perfiles == null || perfiles.isEmpty()){
			model.addAttribute("usuarios", usuarioService.buscarUsuarios());
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.perfiles",new Object[]{},new Locale("")));
			model.addAttribute("usuario",usuarioSession);
			return "usuarios";
		}
		model.addAttribute("roles",roles);
		model.addAttribute("perfiles",perfiles);
		model.addAttribute("clientes",clientes);
		return "crearUsuario";
	}
	
	@PostMapping(path="/crearUsuario")
	public @ResponseBody ModelAndView createUsuario (@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam Long rol,
			@RequestParam Long perfil,
			@RequestParam Long cliente) {

		ModelAndView model = new ModelAndView("usuarios");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		model.addObject("usuario",usuarioSession);

		Usuario usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setAlias(alias);
		usuario.setEmail(email);
		usuario.setPassword(password);
		usuario.setRol(rolService.buscarPorId(rol));
		usuario.setPerfil(perfilService.buscarPorId(perfil));
		
		Cliente clienteSel = clienteService.buscarPorId(cliente);
		if(perfil.intValue() == Constantes.PERFIL_CLIENTE){
			clienteSel.setRepresentante(usuario);
		} else if (perfil.intValue() == Constantes.PERFIL_CONTADOR){
			clienteSel.setContador(usuario);
		}
		clienteService.guardar(clienteSel);
		if(!Constantes.GUARDADO.equalsIgnoreCase(usuarioService.crearUsuario(usuario))){
			model.addObject("usuarios", usuarioService.buscarUsuarios());
			model.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			model.addObject("salida", messageSource.getMessage("usuario.guardado.error",new Object[]{},new Locale("")));
			return model;
		}
		model.addObject("usuarios", usuarioService.buscarUsuarios());
		model.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		model.addObject("salida", messageSource.getMessage("usuario.creado.exito",new Object[]{},new Locale("")));
		return model;
	}
	
	@RequestMapping("/editarUsuario")
	public String editarUsuario(@RequestParam(value="id") Long id, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		if(usuarioSession.isAdmin()) {
			List<Rol> roles = (List<Rol>) rolService.getAllRoles();
			if(roles == null || roles.isEmpty()){
				model.addAttribute("usuarios", usuarioService.buscarUsuarios());
				model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
				model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
				model.addAttribute("usuario",usuarioSession);
				return "usuarios";
			}
			List<Perfil> perfiles = (List<Perfil>) perfilService.getAllPerfiles();
			if(perfiles == null || perfiles.isEmpty()){
				model.addAttribute("usuarios", usuarioService.buscarUsuarios());
				model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
				model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.perfiles",new Object[]{},new Locale("")));
				model.addAttribute("usuario",usuarioSession);
				return "usuarios";
			}
			List<Cliente> clientes = (List<Cliente>) clienteService.buscarClientes();
			model.addAttribute("roles",roles);
			model.addAttribute("perfiles",perfiles);
			model.addAttribute("clientes",clientes);
		} else if (usuarioSession.getId() != id){
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("usuario.sin.permisos",new Object[]{},new Locale("")));
			return "mensaje";
		}
		model.addAttribute("usuario", usuarioService.buscarPorId(id));
		return "editarUsuario";
	}

	@PostMapping(path="/editarUsuario")
	public @ResponseBody ModelAndView editarUsuario (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam Long rol,
			@RequestParam Long perfil,
			@RequestParam Long cliente) throws ParseException {
		
		ModelAndView result = new ModelAndView("usuarios");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		Usuario usuario = usuarioService.buscarPorId(id);
		usuario.setNombre(nombre);
		usuario.setAlias(alias);
		usuario.setEmail(email);
		usuario.setRol(rolService.buscarPorId(rol));
		usuario.setPerfil(perfilService.buscarPorId(perfil));
		
		Cliente clienteSel = clienteService.buscarPorId(cliente);
		if(perfil.intValue() == Constantes.PERFIL_CLIENTE){
			clienteSel.setRepresentante(usuario);
		} else if (perfil.intValue() == Constantes.PERFIL_CONTADOR){
			clienteSel.setContador(usuario);
		}
		clienteService.guardar(clienteSel);
		if(!Constantes.GUARDADO.equalsIgnoreCase(usuarioService.updateUsuario(usuario))){
			result.addObject("usuarios", usuarioService.buscarUsuarios());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("usuario.guardado.error",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("usuarios", usuarioService.buscarUsuarios());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("usuario.creado.exito",new Object[]{usuario.getId()},new Locale("")));
		return result;
	}
	
	@RequestMapping("/cambiarPassword")
	public String cambiarPassword(@RequestParam(value="id") Long id, Model model) {
		Usuario usuario = usuarioService.buscarPorId(id);
		if(usuario == null){
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("error.usuario.no.encontrado",new Object[]{},new Locale("")));
			return "mensaje";
		}
		model.addAttribute("usuario",usuario);
		return "cambiarPassword";
	}
	
	@PostMapping(path="/cambiarPassword")
	public @ResponseBody ModelAndView cambiarPassword (@RequestParam Long id,
			@RequestParam String oldPassword,
			@RequestParam String newPassword1,
			@RequestParam String newPassword2) throws ParseException {
		
		ModelAndView result = new ModelAndView();
		if(!newPassword1.equalsIgnoreCase(newPassword2)){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("newPassword.no.coincide",new Object[]{},new Locale("")));
			result.setViewName("cambiarPassword");
			return result;
		}
		Usuario usuario = usuarioService.buscarPorId(id);
		if(usuario == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("error.usuario.no.encontrado",new Object[]{},new Locale("")));
			result.setViewName("mensaje");
			return result;
		}
		if(usuario.getPassword().equalsIgnoreCase(oldPassword)){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("password.cambiada.error",new Object[]{},new Locale("")));
			result.setViewName("mensaje");
			return result;
		} 
		usuario.setPassword(newPassword1);
		if(!Constantes.GUARDADO.equalsIgnoreCase(usuarioService.updateUsuario(usuario))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("oldPpassword.no.coincide",new Object[]{},new Locale("")));
			result.setViewName("cambiarPassword");
			return result;
		}
		result.addObject("usuarios", usuarioService.buscarUsuarios());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("password.cambiada.exito",new Object[]{},new Locale("")));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		result.setViewName("usuarios");
		return result;
	}
	
	@RequestMapping("/eliminarUsuario")
	public ModelAndView eliminarUsuario(@RequestParam(value="id") Long id) {
		ModelAndView result = new ModelAndView("usuarios");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		Usuario usuario = usuarioService.buscarPorId(id);
		if(usuario == null){
			result.addObject("usuarios", usuarioService.buscarUsuarios());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("error.usuario.no.encontrado",new Object[]{},new Locale("")));
			return result;
		}
		usuario.setFechaBaja(new Date());
		if(!Constantes.ELIMINADO.equalsIgnoreCase(usuarioService.eliminarUsuario(usuario))){
			result.addObject("usuarios", usuarioService.buscarUsuarios());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("usuario.baja.error",new Object[]{usuario.getNombre()},new Locale("")));
			return result;
		}
		result.addObject("usuarios", usuarioService.buscarUsuarios());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("usuario.baja.exito",new Object[]{usuario.getNombre()},new Locale("")));
		return result;
	}
	
	@RequestMapping("/usuarios")
	public String usuarios(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		if(!usuario.isAdmin()) {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("usuario.sin.permisos",new Object[]{},new Locale("")));
			return "mensaje";
		}
		model.addAttribute("usuario",usuario);
		model.addAttribute("usuarios", usuarioService.buscarUsuarios());
		return "usuarios";
	}
}
