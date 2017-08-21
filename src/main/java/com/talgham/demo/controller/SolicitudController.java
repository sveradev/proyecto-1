
package com.talgham.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import com.talgham.demo.model.Email;
import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.EmailService;
import com.talgham.demo.service.EstadoService;
import com.talgham.demo.service.SolicitudService;
import com.talgham.demo.service.UsuarioService;

@Controller
public class SolicitudController {

	@Autowired
	private SolicitudService solicitudService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private EstadoService estadoService;

	@GetMapping("/crearSolicitud")
	public String solicitud(Model model) {
		Long idRol = new Long(2);
		ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(idRol);
		model.addAttribute("responsables",responsables);
		return "crearSolicitud";
	}

	@PostMapping(path="/crearSolicitud")
	public @ResponseBody ModelAndView addSolicitud (
			@RequestParam String nombre,
			@RequestParam String titulo,
			@RequestParam String email,
			@RequestParam String descripcion,
			@RequestParam String responsable,
			@RequestParam String fechaSol
			) throws ParseException {
		
		Date date = new Date();
		
		if(fechaSol!= null && !fechaSol.equalsIgnoreCase("")){
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			date = formatter.parse(fechaSol);
		}
		
		Solicitud solicitud = solicitudService.addSolicitud(nombre, titulo, email, descripcion,responsable,date);
		Email emailTemplate = emailService.buscarPorProceso("SolicitudNueva");
		if(emailTemplate != null){
			String to = emailTemplate.getEmail();
			String subject = emailTemplate.getSubject();
			String texto = emailTemplate.getTexto();
		
			try {
				emailService.sendEmail(to, subject, texto);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ModelAndView result = new ModelAndView();
//		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.creada.exito"));
		result.addObject("mensaje", "Su solicitud se ha generado con éxito. Muchas Gracias.");

		result.setViewName("mensaje");
		
		return result;
	}
	
	@RequestMapping("/editarSolicitud")
	public String editarSolicitud(@RequestParam(value="id") Long id, Model model) {
		Solicitud solicitud = solicitudService.buscarPorId(id);
		
		Date date = solicitud.getFechaSolicitado();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String fechaSol = formatter.format(date);
		
		model.addAttribute("fechaSol", fechaSol);
		model.addAttribute("solicitud", solicitudService.buscarPorId(id));
		ArrayList<Estado> estados = (ArrayList<Estado>) estadoService.getAllEstados();
		Long idRol = new Long(2);
		ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(idRol);
		model.addAttribute("responsables",responsables);
		model.addAttribute("estados",estados);
		return "editarSolicitud";
	}

	@PostMapping(path="/editarSolicitud")
	public @ResponseBody ModelAndView editarSolicitud (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String estado,
			@RequestParam String titulo,
			@RequestParam String email,
			@RequestParam String descripcion, 
			@RequestParam String responsable,
			@RequestParam String fechaSol ) throws ParseException {
		
		Date fechaSolicitado = new Date();
		if(fechaSol!= null && !fechaSol.equalsIgnoreCase("")){
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			fechaSolicitado = formatter.parse(fechaSol);
		}
		
		Solicitud solicitud = new Solicitud();
		solicitud.setId(id);
		solicitud.setNombre(nombre);
		solicitud.setTitulo(titulo);
		solicitud.setEmail(email);
		solicitud.setDescripcion(descripcion);
		solicitud.setResponsable(responsable);
		solicitud.setEstado(estado);
		solicitud.setFechaSolicitado(fechaSolicitado);
		solicitud.setFechaModificado(new Date());
		if(estado!= null && estado.equalsIgnoreCase("FINALIZADO")){
			solicitud.setFechaFinalizado(new Date());
		}
		
		solicitudService.updateSolicitud(solicitud);
		
		ModelAndView result = new ModelAndView();
//		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.editada.exito",id));
		result.addObject("mensaje", "La solicitud "+ id +" se ha modificado con éxito. Muchas Gracias.");
		result.setViewName("mensaje");
		
		return result;
	}

	@RequestMapping("/solicitudes")
	public String solicitudes(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {

		ArrayList<Solicitud> solicitudes = (ArrayList<Solicitud>) solicitudService.getAllSolicitudes();
		model.addAttribute("solicitudes", solicitudes);
		return "solicitudes";
	}
}
