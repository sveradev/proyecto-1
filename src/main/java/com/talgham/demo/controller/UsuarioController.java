package com.talgham.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Rol;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.RolService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private RolService rolService;
	
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
		ModelAndView model = new ModelAndView("crearUsuario");

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
			@RequestParam String rol) throws ParseException {
		
		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setNombre(nombre);
		usuario.setAlias(alias);
		usuario.setEmail(email);
		usuario.setRol(rol);
		
		usuarioService.updateUsuario(usuario);
		
		ModelAndView result = new ModelAndView();
//		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.editada.exito",id));
		result.addObject("mensaje", "La solicitud "+ id +" se ha modificado con éxito. Muchas Gracias.");
		result.setViewName("mensaje");
		
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