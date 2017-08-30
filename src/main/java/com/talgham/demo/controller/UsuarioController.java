package com.talgham.demo.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

		ArrayList<Rol> roles = (ArrayList<Rol>) rolService.getAllRoles();
		model.addAttribute("roles",roles);
		return "crearUsuario";
	}

	@PostMapping(path="/crearUsuario")
	public @ResponseBody ModelAndView createUsuario (@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam String rol) {

		String response = usuarioService.crearUsuario(nombre, alias,email,password);
		ModelAndView model = new ModelAndView("usuarios");

		if(response.equalsIgnoreCase("saved")){
			model.addObject("showModal", Boolean.TRUE);
			model.addObject("msgModalSalida", "Guadado");
		}
		return model;
	}
	
	@RequestMapping("/editarUsuario")
	public String editarUsuario(@RequestParam(value="id") Long id, Model model) {
		model.addAttribute("usuario", usuarioService.buscarUsuarioPorId(id));
		ArrayList<Rol> roles = (ArrayList<Rol>) rolService.getAllRoles();
		model.addAttribute("roles",roles);
		return "editarUsuario";
	}

	@PostMapping(path="/editarUsuario")
	public @ResponseBody ModelAndView editarUsuario (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam Long rol) throws ParseException {
		
		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setNombre(nombre);
		usuario.setAlias(alias);
		usuario.setEmail(email);
		Rol rolSelected = rolService.buscarRolesPorId(rol);
		usuario.setRol(rolSelected);
		
		usuarioService.updateUsuario(usuario);
		
		ModelAndView result = new ModelAndView();
		result.addObject("tipoSalida","alert-success");
		result.addObject("salida", messageSource.getMessage("usuario.creado.exito",new Object[]{usuario.getId()},new Locale("")));
		result.setViewName("mensaje");
		
		return result;
	}
	
	@RequestMapping("/cambiarPassword")
	public String cambiarPassword(@RequestParam(value="id") Long id, Model model) {
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
		if(usuario == null){
			model.addAttribute("tipoSalida","alert-danger");
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
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
		if(!newPassword1.equalsIgnoreCase(newPassword2)){
			result.addObject("tipoSalida","alert-danger");
			result.addObject("salida", messageSource.getMessage("newPassword.no.coincide",new Object[]{},new Locale("")));
			result.setViewName("cambiarPassword");
			return result;
		}
		if(usuario != null){
			if(usuario.getPassword().equalsIgnoreCase(oldPassword)){
				usuario.setPassword(newPassword1);
				usuarioService.updateUsuario(usuario);
			} else {
				result.addObject("tipoSalida","alert-danger");
				result.addObject("salida", messageSource.getMessage("oldPpassword.no.coincide",new Object[]{},new Locale("")));
				result.setViewName("cambiarPassword");
				return result;
			}
		} else {
			result.addObject("tipoSalida","alert-danger");
			result.addObject("salida", messageSource.getMessage("error.usuario.no.encontrado",new Object[]{},new Locale("")));
			result.setViewName("mensaje");
			return result;
		}
		
		result.addObject("tipoSalida","alert-success");
		result.addObject("salida", messageSource.getMessage("password.cambiada.exito",new Object[]{},new Locale("")));
		result.setViewName("Solicitudes");
		return result;
	}
	
	@RequestMapping("/eliminarUsuario")
	public ModelAndView eliminarUsuario(@RequestParam(value="id") Long id, Model model) {
		ModelAndView result = usuarios();
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
		usuario.setFechaBaja(new Date());
		usuarioService.updateUsuario(usuario);
		
		result.addObject("tipoSalida","alert-success");
		result.addObject("salida","El usuario "+usuario.getNombre()+" se ha dado de baja. Muchas Gracias.");
		return result;
	}
	
	@RequestMapping("/usuarios")
	public String usuarios(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {
		ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usuarioService.buscarUsuarios();
		model.addAttribute("usuarios", usuarios);
		return "usuarios";
	}
	
	public ModelAndView usuarios() {
		ModelAndView result = new ModelAndView();
		result.setViewName("usuarios");
		ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usuarioService.buscarUsuarios();
		result.addObject("usuarios", usuarios);
		return result;
	}

}