
package com.talgham.demo.controller;

import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.model.Email;
import com.talgham.demo.service.EmailService;

@Controller
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@GetMapping("/crearEmail")
	public String crearEmail(Model model) {
		return "crearEmail";
	}

	@PostMapping(path="/crearEmail")
	public @ResponseBody ModelAndView addEmail (
			@RequestParam String email,
			@RequestParam String proceso,
			@RequestParam String subject,
			@RequestParam String texto
			) throws ParseException {
		
		Email emailModel = emailService.addEmail(email, proceso, subject, texto);
		
		ModelAndView result = this.emails();
		result.addObject("tipoSalida","alert-success");
		result.addObject("salida", "La configuracion del Email para el proceso "+emailModel.getProceso()+" se ha realizado con éxito. Muchas Gracias.");
		
		return result;
	}
//	
//	@RequestMapping("/editarSolicitud")
//	public String editarSolicitud(@RequestParam(value="id") Long id, Model model) {
//		Solicitud solicitud = solicitudService.buscarPorId(id);
//		
//		Date date = solicitud.getFechaSolicitado();
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		String fechaSol = formatter.format(date);
//		
//		model.addAttribute("fechaSol", fechaSol);
//		model.addAttribute("solicitud", solicitudService.buscarPorId(id));
//		ArrayList<Estado> estados = (ArrayList<Estado>) estadoService.getAllEstados();
//		Long idRol = new Long(3);
//		Rol rol = rolService.buscarRolesPorId(idRol);
//		ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(rol.getNombre());
//		model.addAttribute("responsables",responsables);
//		model.addAttribute("estados",estados);
//		return "editarSolicitud";
//	}
//
//	@PostMapping(path="/editarSolicitud")
//	public @ResponseBody ModelAndView editarSolicitud (@RequestParam Long id,
//			@RequestParam String nombre,
//			@RequestParam String estado,
//			@RequestParam String titulo,
//			@RequestParam String email,
//			@RequestParam String descripcion, 
//			@RequestParam String responsable,
//			@RequestParam String fechaSol ) throws ParseException {
//		
//		Date fechaSolicitado = new Date();
//		if(fechaSol!= null && !fechaSol.equalsIgnoreCase("")){
//			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//			fechaSolicitado = formatter.parse(fechaSol);
//		}
//		
//		Solicitud solicitud = new Solicitud();
//		solicitud.setId(id);
//		solicitud.setNombre(nombre);
//		solicitud.setTitulo(titulo);
//		solicitud.setEmail(email);
//		solicitud.setDescripcion(descripcion);
//		solicitud.setResponsable(responsable);
//		solicitud.setEstado(estado);
//		solicitud.setFechaSolicitado(fechaSolicitado);
//		solicitud.setFechaModificado(new Date());
//		if(estado!= null && estado.equalsIgnoreCase("FINALIZADO")){
//			solicitud.setFechaFinalizado(new Date());
//		}
//		
//		solicitudService.updateSolicitud(solicitud);
//		
//		ModelAndView result = new ModelAndView();
////		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.editada.exito",id));
//		result.addObject("mensaje", "La solicitud "+ id +" se ha modificado con éxito. Muchas Gracias.");
//		result.setViewName("mensaje");
//		
//		return result;
//	}

	@RequestMapping("/emails")
	public String emails(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {

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
