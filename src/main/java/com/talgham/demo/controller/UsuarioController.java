package com.talgham.demo.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Rol;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.RolService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private RolService rolService;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/crearUsuario")
	public String usuario(Model model) {
		model.addAttribute("showModal", Boolean.FALSE);
		model.addAttribute("msgModalSalida", "");

		List<Rol> roles = (List<Rol>) rolService.getAllRoles();
		if(roles != null && !roles.isEmpty()){
			model.addAttribute("roles",roles);
		} else {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
		}
		return "crearUsuario";
	}

	@PostMapping(path="/crearUsuario")
	public @ResponseBody ModelAndView createUsuario (@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam String rol) {

		ModelAndView model = new ModelAndView("usuarios");
		Usuario usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setAlias(alias);
		usuario.setEmail(email);
		usuario.setPassword(password);
		if(usuarioService.crearUsuario(usuario).equalsIgnoreCase(Constantes.GUARDADO)){
			model.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
			model.addObject("salida", messageSource.getMessage("usuario.creado.exito",new Object[]{},new Locale("")));
		} else {
			model.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			model.addObject("salida", messageSource.getMessage("usuario.guardado.error",new Object[]{},new Locale("")));
		}
		return model;
	}
	
	@RequestMapping("/editarUsuario")
	public String editarUsuario(@RequestParam(value="id") Long id, Model model) {
		model.addAttribute("usuario", usuarioService.buscarUsuarioPorId(id));
		List<Rol> roles = (List<Rol>) rolService.getAllRoles();
		if(roles != null && !roles.isEmpty()){
			model.addAttribute("roles",roles);
		} else {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
		}
		return "editarUsuario";
	}

	@PostMapping(path="/editarUsuario")
	public @ResponseBody ModelAndView editarUsuario (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam Long rol) throws ParseException {
		
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
		usuario.setNombre(nombre);
		usuario.setAlias(alias);
		usuario.setEmail(email);
		Rol rolSelected = rolService.buscarRolesPorId(rol);
		usuario.setRol(rolSelected);
		
		ModelAndView result = new ModelAndView("usuarios");
		if(usuarioService.updateUsuario(usuario).equalsIgnoreCase(Constantes.GUARDADO)){
			result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
			result.addObject("salida", messageSource.getMessage("usuario.creado.exito",new Object[]{usuario.getId()},new Locale("")));
		} else {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("usuario.guardado.error",new Object[]{},new Locale("")));
		}
		return result;
	}
	
	@RequestMapping("/cambiarPassword")
	public String cambiarPassword(@RequestParam(value="id") Long id, Model model) {
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
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
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
		if(usuario != null){
			if(usuario.getPassword().equalsIgnoreCase(oldPassword)){
				usuario.setPassword(newPassword1);
				if(usuarioService.updateUsuario(usuario).equalsIgnoreCase(Constantes.GUARDADO)){
					result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
					result.addObject("salida", messageSource.getMessage("password.cambiada.exito",new Object[]{},new Locale("")));
					result.setViewName("Solicitudes");
				} else {
					result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
					result.addObject("salida", messageSource.getMessage("password.cambiada.error",new Object[]{},new Locale("")));
					result.setViewName("mensaje");
				}
			} else {
				result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
				result.addObject("salida", messageSource.getMessage("oldPpassword.no.coincide",new Object[]{},new Locale("")));
				result.setViewName("cambiarPassword");
			}
		} else {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("error.usuario.no.encontrado",new Object[]{},new Locale("")));
			result.setViewName("mensaje");
		}
		
		return result;
	}
	
	@RequestMapping("/eliminarUsuario")
	public ModelAndView eliminarUsuario(@RequestParam(value="id") Long id) {
		ModelAndView result = new ModelAndView("usuarios");
		result.addObject("usuarios", usuarioService.buscarUsuarios());
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
		if(usuario != null){
			usuario.setFechaBaja(new Date());
			if(usuarioService.updateUsuario(usuario).equalsIgnoreCase(Constantes.GUARDADO)){
				result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
				result.addObject("salida", messageSource.getMessage("usuario.baja.exito",new Object[]{usuario.getNombre()},new Locale("")));
			} else {
				result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
				result.addObject("salida", messageSource.getMessage("usuario.baja.error",new Object[]{usuario.getNombre()},new Locale("")));
			}
		} else {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("error.usuario.no.encontrado",new Object[]{},new Locale("")));
		}
		return result;
	}
	
	@RequestMapping("/usuarios")
	public String usuarios(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {
		model.addAttribute("usuarios", usuarioService.buscarUsuarios());
		return "usuarios";
	}
}