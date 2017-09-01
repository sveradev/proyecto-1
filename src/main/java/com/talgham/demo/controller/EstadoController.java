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
import com.talgham.demo.model.Estado;
import com.talgham.demo.service.EstadoService;

@Controller
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	MessageSource messageSource;

	@RequestMapping("/crearEstado")
	public String estado(Model model) {
		return "crearEstado";
	}

	@PostMapping(path="/crearEstado")
	public @ResponseBody ModelAndView crearEstado (@RequestParam String nombre,
			@RequestParam Integer orden) {

		ModelAndView result = new ModelAndView("estados");
		Estado estado = new Estado();
		estado.setNombre(nombre);
		estado.setOrden(orden);
		estado.setFechaCreacion(new Date());
		String response = estadoService.crearEstado(estado);

		if(response != null && response.equalsIgnoreCase(Constantes.GUARDADO)){
			result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
			result.addObject("salida", messageSource.getMessage("estado.creado.exito",new Object[]{},new Locale("")));
		} else {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("estado.no.creado.error",new Object[]{},new Locale("")));
		}
		return result;
	}
	
	@RequestMapping("/estados")
	public String estados(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {
		model.addAttribute("estados", estadoService.getAllEstados(););
		return "estados";
	}
}
