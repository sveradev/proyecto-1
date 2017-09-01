package com.talgham.demo.controller;

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

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Rol;
import com.talgham.demo.service.RolService;

@Controller
public class RolController {

	@Autowired
	private RolService rolService;
	@Autowired
	MessageSource messageSource;
	
	@RequestMapping("/crearRol")
	public String rol(Model model) {
		return "crearRol";
	}

	@PostMapping(path="/crearRol")
	public @ResponseBody ModelAndView crearRol (@RequestParam String nombre,
			@RequestParam Integer orden) {

		ModelAndView result = new ModelAndView("roles");
		Rol rol = new Rol();
		rol.setNombre(nombre);
		rol.setOrden(orden);
		rol.setFechaCreacion(new Date());

		if(!Constantes.GUARDADO.equalsIgnoreCase(rolService.crearRol(rol))){
			result.addObject("roles", rolService.getAllRoles());
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("rol.no.creado.error",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("roles", rolService.getAllRoles());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("rol.creado.exito",new Object[]{},new Locale("")));
		return result;
	}
	
	@RequestMapping("/roles")
	public String roles(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {
		model.addAttribute("roles", rolService.getAllRoles());
		return "roles";
	}
}
