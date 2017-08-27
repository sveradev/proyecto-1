
package com.talgham.demo.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
		List<Actividad> actividades = (List<Actividad>) actividadService.getAllActividades();
		model.addAttribute("actividades", actividades);
		return "crearEmail";
	}

	@PostMapping(path="/crearEmail")
	public @ResponseBody ModelAndView addEmail (
			@RequestParam String direccion,
			@RequestParam Long actividad,
			@RequestParam String subject,
			@RequestParam String texto
			) throws ParseException {
		
		Email email =  new Email();
		email.setDireccion(direccion);
		email.setActividad(actividadService.buscarPorId(actividad));
		email.setSubject(subject);
		email.setTexto(texto);
		
		emailService.addEmail(email);
		
		ModelAndView result = this.emails();
		result.addObject("tipoSalida","alert-success");
		result.addObject("salida", "La configuracion del Email se ha realizado con éxito. Muchas Gracias.");
		
		return result;
	}
	
	@RequestMapping("/editarEmail")
	public String editarEmail(@RequestParam(value="id") Long id, Model model) {
		model.addAttribute("email", emailService.buscarPorId(id));
		List<Actividad> actividades = (List<Actividad>) actividadService.getAllActividades();
		model.addAttribute("actividades", actividades);
		return "editarEmail";
	}

	@PostMapping(path="/editarEmail")
	public @ResponseBody ModelAndView editarEmail (@RequestParam Long id,
			@RequestParam String direccion,
			@RequestParam Long actividad,
			@RequestParam String subject,
			@RequestParam String texto) throws ParseException {
				
		Email email = emailService.buscarPorId(id);
		Actividad actividadSeleccionada = actividadService.buscarPorId(actividad);
		email.setDireccion(direccion);
		email.setActividad(actividadSeleccionada);
		email.setSubject(subject);
		email.setTexto(texto);
		email.setFechaModificacion(new Date());
		
		emailService.guardarEmail(email);
		
		ModelAndView result = new ModelAndView();
//		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.editada.exito",id));
		result.addObject("tipoSalida", "alert-success");
		result.addObject("salida", "El Email para la actividad"+ actividad +" se ha modificado con éxito. Muchas Gracias.");
		result.setViewName("emails");
		
		return result;
	}

	@RequestMapping("/emails")
	public String emails(Model model) {

		ArrayList<Email> emails = (ArrayList<Email>) emailService.getAllEmails();
		model.addAttribute("emails", emails);
		return "emails";
	}
	
	public ModelAndView emails() {
		ModelAndView result = new ModelAndView();
		ArrayList<Email> emails = (ArrayList<Email>) emailService.getAllEmails();
		result.setViewName("emails");
		result.addObject("emails", emails);
		return result;
	}
}
