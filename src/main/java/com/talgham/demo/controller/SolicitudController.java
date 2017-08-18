
package com.talgham.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.common.MessageSourceManager;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.service.EmailService;
import com.talgham.demo.service.SolicitudService;

@Controller
public class SolicitudController {

	@Autowired
	private SolicitudService solicitudService;
	@Autowired
	private EmailService emailService;

	@GetMapping("/crearSolicitud")
	public String solicitud(Model model) {
		return "crearSolicitud";
	}

	@PostMapping(path="/crearSolicitud")
	public @ResponseBody ModelAndView addSolicitud (@RequestParam String nombre,
			@RequestParam String titulo,
			@RequestParam String email,
			@RequestParam String descripcion) {

		Solicitud solicitud = solicitudService.addSolicitud(nombre, titulo, email, descripcion);
//		Email email = emailRepository.findByProceso("SolicitudNueva");
//		if(email != null){
//			String to = email.getEmail();
//			String subject = email.getSubject();
//			String text = email.getTexto();
//		}
		
		try {
//			emailService.sendEmail(to, subject, texto);
			emailService.sendEmail("//julian.n.vera@gmail.com", "Solicitid " + solicitud.getNombre() + " creada", "Hola, se creo la solicitud " + solicitud.getId() + " con estado " + solicitud.getEstado());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// new ModelAndView("userAdd", "command", newUser);
		
		ModelAndView result = new ModelAndView();
		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.creada.exito"));
//		result.addObject("mensaje", "<p>Su solicitud se ha generado con &eacute;xito.</p><p>Muchas Gracias.</p>");
		result.setViewName("mensaje");
		
		return result;
	}
	
	@RequestMapping("/editarSolicitud")
	public String editarSolicitud(@RequestParam(value="id") Long id, Model model) {
		model.addAttribute("solicitud", solicitudService.buscarPorId(id));
		return "editarSolicitud";
	}

	@PostMapping(path="/editarSolicitud")
	public @ResponseBody ModelAndView editarSolicitud (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String estado,
			@RequestParam String titulo,
			@RequestParam String email,
			@RequestParam String descripcion, 
			@RequestParam String responsable) {

		solicitudService.updateSolicitud(id, estado, nombre, titulo, email, descripcion ,responsable);
		
		ModelAndView result = new ModelAndView();
		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.editada.exito",id));
		//result.addObject("mensaje", "<p>La solicitud "+ id +" se ha modificado con &eacute;xito.</p><p>Muchas Gracias.</p>");
		result.setViewName("mensaje");
		
		return result;
	}
}
