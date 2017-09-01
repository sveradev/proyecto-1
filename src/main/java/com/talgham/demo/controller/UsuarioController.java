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

		List<Rol> roles = (List<Rol>) rolService.getAllRoles();
		if(roles == null && roles.isEmpty()){
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
			return "usuarios";
		}
		model.addAttribute("roles",roles);
		return "crearUsuario";
	}

	@PostMapping(path="/crearUsuario")
	public @ResponseBody ModelAndView createUsuario (@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam Long rol) {

		ModelAndView model = new ModelAndView("usuarios");
		Usuario usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setAlias(alias);
		usuario.setEmail(email);
		usuario.setPassword(password);
		ususario.setRol(rolService.buscarPorId(rol));
		if(!Constantes.GUARDADO.equalsIgnoreCase(usuarioService.crearUsuario(usuario))){
			model.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			model.addObject("salida", messageSource.getMessage("usuario.guardado.error",new Object[]{},new Locale("")));
			return model;
		}
		model.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		model.addObject("salida", messageSource.getMessage("usuario.creado.exito",new Object[]{},new Locale("")));
		return model;
	}
	
	@RequestMapping("/editarUsuario")
	public String editarUsuario(@RequestParam(value="id") Long id, Model model) {
		model.addAttribute("usuario", usuarioService.buscarPorId(id));
		List<Rol> roles = (List<Rol>) rolService.getAllRoles();
		if(roles == null && roles.isEmpty()){
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
			return "usuarios";
		}
		model.addAttribute("roles",roles);
		return "editarUsuario";
	}

	@PostMapping(path="/editarUsuario")
	public @ResponseBody ModelAndView editarUsuario (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam Long rol) throws ParseException {
		
		ModelAndView result = new ModelAndView("usuarios");
		
		Usuario usuario = usuarioService.buscarPorId(id);
		usuario.setNombre(nombre);
		usuario.setAlias(alias);
		usuario.setEmail(email);
		usuario.setRol(rolService.buscarPorId(rol));
		
		if(!Constantes.GUARDADO.equalsIgnoreCase(usuarioService.updateUsuario(usuario))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("usuario.guardado.error",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("usuario.creado.exito",new Object[]{usuario.getId()},new Locale("")));
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
		if(usuario == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("error.usuario.no.encontrado",new Object[]{},new Locale("")));
			result.setViewName("mensaje");
			return result;
		}
		if(oldPassword != null && !oldPassword.equalsIgnoreCase(usuario.getPassword())){
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
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("password.cambiada.exito",new Object[]{},new Locale("")));
		result.setViewName("Solicitudes");
		return result;
	}
	
	@RequestMapping("/eliminarUsuario")
	public ModelAndView eliminarUsuario(@RequestParam(value="id") Long id) {
		ModelAndView result = new ModelAndView("usuarios");
		result.addObject("usuarios", usuarioService.buscarUsuarios());
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
		if(usuario == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("error.usuario.no.encontrado",new Object[]{},new Locale("")));
			return result;
		}
		usuario.setFechaBaja(new Date());
		if(Constantes.GUARDADO.equalsIgnoreCase(usuarioService.updateUsuario(usuario))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("usuario.baja.error",new Object[]{usuario.getNombre()},new Locale("")));
			return result;
		}
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("usuario.baja.exito",new Object[]{usuario.getNombre()},new Locale("")));
		return result;
	}
	
	@RequestMapping("/usuarios")
	public String usuarios(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {
		model.addAttribute("usuarios", usuarioService.buscarUsuarios());
		return "usuarios";
	}
}
