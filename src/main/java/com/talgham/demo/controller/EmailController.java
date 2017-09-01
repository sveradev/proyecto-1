package com.talgham.demo.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.model.Actividad;
import com.talgham.demo.model.Email;
import com.talgham.demo.service.ActividadService;
import com.talgham.demo.service.EmailService;

@Controller
public class EmailController {

	@Autowired
	private EmailService emailService;
	@Autowired
	private ActividadService actividadService;
	
	@GetMapping("/crearEmail")
	public String crearEmail(Model model) {
		model.addAttribute("actividades", actividadService.getAllActividades());
		return "crearEmail";
	}

	@PostMapping(path="/crearEmail")
	public @ResponseBody ModelAndView addEmail (
			@RequestParam String direccion,
			@RequestParam Actividad actividad,
			@RequestParam String subject,
			@RequestParam String texto) throws ParseException {
		
		ModelAndView result = new ModelAndView("emails");
		
		Email email = new Email();
		email.setDireccion(direccion);
		email.setActividad(actividad);
		email.setSubject(subject);
		email.setTexto(texto);
		if(emailService.addEmail(email).equalsIgnoreCase(Constantes.GUARDADO){
			result.addObject("emails", emailService.getAllEmails());
			result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
			result.addObject("salida", messageSource.getMessage("email.creada.exito",new Object[]{},new Locale("")));
		} else {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("email.no.guardado.error",new Object[]{},new Locale("")));
		}
		return result;
	}
	
	@RequestMapping("/editarEmail")
	public String editarEmail(@RequestParam(value="id") Long id, Model model) {
		Email email = emailService.buscarPorId(id);
		model.addAttribute("email", email);
		List<Actividad> actividades = (List<Actividad>) actividadService.getAllActividades();
		if(actividades != null && !actividades.isEmpty()){
			model.addAttribute("actividades", actividades);
		} else {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("actividad.no.exite",new Object[]{},new Locale("")));
		}
		return "editarEmail";
	}

	@PostMapping(path="/editarEmail")
	public @ResponseBody ModelAndView editarEmail (@RequestParam Long id,
			@RequestParam String direccion,
			@RequestParam Actividad actividad,
			@RequestParam String subject,
			@RequestParam String texto) throws ParseException {
		
		ModelAndView result = new ModelAndView("emails");		
		
		Email email = emailService.buscarPorId(id);
		email.setDireccion(direccion);
		email.setActividad(actividad);
		email.setSubject(subject);
		email.setTexto(texto);
		if(emailService.guardarEmail(email).equalsIgnoreCase(Constantes.GUARDADO)){
			result.addObject("tipoSalida", Constantes.ALERTA_SUCCESS);
			result.addObject("salida", messageSource.getMessage("email.guardado.exito",new Object[]{},new Locale("")));
		} else {
			result.addObject("tipoSalida", Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("email.no.guardado.error",new Object[]{},new Locale("")));
		}		
		return result;
	}

	@RequestMapping("/emails")
	public String emails(Model model) {
		model.addAttribute("emails", emailService.getAllEmails());
		return "emails";
	}
}
