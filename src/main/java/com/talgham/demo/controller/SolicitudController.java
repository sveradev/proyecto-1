
package com.talgham.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.talgham.demo.model.Email;
import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Rol;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.EmailService;
import com.talgham.demo.service.EstadoService;
import com.talgham.demo.service.RolService;
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
	@Autowired
	private RolService rolService;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	
	@GetMapping("/crearSolicitud")
	public String crearSolicitud(Model model) {
		Long idRol = new Long(3);
		Rol rol = rolService.buscarRolesPorId(idRol);
		if(rol != null){
			ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(rol.getNombre());
			if(!responsables.isEmpty()){
				model.addAttribute("responsables",responsables);
			} else {
				model.addAttribute("tipoSalida","alert-danger");
				model.addAttribute("salida","No existen empleados para tomar su solicitud. Muchas Gracias.");
			}
		} else {
			model.addAttribute("tipoSalida","alert-danger");
			model.addAttribute("salida","No existen empleados para tomar su solicitud. Muchas Gracias.");
		}
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
				e.printStackTrace();
			}
		}
		
		ModelAndView result = solicitudes(null);
//		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.creada.exito"));
		result.addObject("tipoSalida","alert-success");
		result.addObject("salida","Su solicitud se ha generado con éxito. Muchas Gracias.");
		
		return result;
	}
	
	@RequestMapping("/editarSolicitud")
	public String editarSolicitud(@RequestParam(value="id") Long id, Model model) {
		Solicitud solicitud = solicitudService.buscarPorId(id);
		
		Date date = solicitud.getFechaSolicitado();
		String fechaSol = formatter.format(date);
		
		model.addAttribute("fechaSol", fechaSol);
		model.addAttribute("solicitud", solicitudService.buscarPorId(id));
		ArrayList<Estado> estados = (ArrayList<Estado>) estadoService.getAllEstados();
		Long idRol = new Long(3);
		Rol rol = rolService.buscarRolesPorId(idRol);
		ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(rol.getNombre());
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
		
		ModelAndView result = solicitudes(null);
//		result.addObject("mensaje", MessageSourceManager.getInstance().getMessage("solicitud.editada.exito",id));
		result.addObject("tipoSalida","alert-success");
		result.addObject("salida","La solicitud "+ id +" se ha modificado con éxito. Muchas Gracias.");
		
		return result;
	}
	
	@GetMapping("/buscarSolicitud")
	public String buscarSolicitud(Model model) {
		Long idRol = new Long(3);
		Rol rol = rolService.buscarRolesPorId(idRol);
		if(rol != null){
			ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(rol.getNombre());
			if(!responsables.isEmpty()){
				model.addAttribute("responsables",responsables);
			}
		}
		return "buscarSolicitud";
	}
	
	@PostMapping(path="/buscarSolicitud")
	public @ResponseBody ModelAndView buscarSolicitud (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String titulo,
			@RequestParam String responsable,
			@RequestParam String fechaDesde,
			@RequestParam String fechaHasta) throws ParseException {
		
		Date solicitadoDesde = null;
		if(fechaDesde!= null && !fechaDesde.equalsIgnoreCase("")){
			solicitadoDesde = formatter.parse(fechaDesde);
		}
		Date solicitadoHasta = null;
		if(fechaHasta!= null && !fechaHasta.equalsIgnoreCase("")){
			solicitadoHasta = formatterTime.parse(fechaHasta+" 23:23:59");
		}
		
		Solicitud solicitud = new Solicitud();
		List<Solicitud> solicitudesResult = new ArrayList<Solicitud>();
		String tipoSalida = "";
		String salida = "";
		
		if(id != null){
			solicitud = solicitudService.buscarPorId(id);
			if(solicitud != null){
				solicitudesResult.add(solicitud);
			} else {
				tipoSalida = "alert-warning";
				salida = "La solicitud "+ id +" no se ha encontrado. Muchas Gracias.";
			}
		} else {
			List<Solicitud> solicitudes = null;
			if((nombre != null && !nombre.trim().equalsIgnoreCase("")) || (titulo != null && !titulo.trim().equalsIgnoreCase("")) || (responsable != null && !responsable.trim().equalsIgnoreCase(""))){
				solicitudes = (List<Solicitud>) solicitudService.buscarPorCampos(nombre,titulo,responsable);
				if(solicitudes == null || solicitudes.isEmpty()){
					tipoSalida = "alert-warning";
					salida = "No se han encontrado solicitudes con los datos ingresados. Muchas Gracias.";
				}
			}
			if(solicitadoDesde != null && solicitadoHasta != null) {
				if(solicitudes == null || solicitudes.isEmpty()){
					solicitudes = (List<Solicitud>) solicitudService.getAllSolicitudes();
				}
				for(Solicitud mySolicitud : solicitudes){
					Date fechaSolicitado = mySolicitud.getFechaSolicitado();
					if(solicitadoDesde.before(fechaSolicitado) && solicitadoHasta.after(fechaSolicitado)){
						solicitudesResult.add(mySolicitud);
					}
				}
				if(solicitudesResult == null || solicitudesResult.isEmpty()){
					tipoSalida = "alert-warning";
					salida = "No se han encontrado solicitudes para los rangos de fecha ingresados. Muchas Gracias.";
				}
			}
		}
		
		ModelAndView result = this.solicitudes(solicitudesResult);
		result.addObject("tipoSalida",tipoSalida);
		result.addObject("salida",salida);
		return result;
	}

	@RequestMapping("/solicitudes")
	public String solicitudes(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {

		ArrayList<Solicitud> solicitudes = (ArrayList<Solicitud>) solicitudService.getAllSolicitudes();
		model.addAttribute("solicitudes", solicitudes);
		return "solicitudes";
	}
	
	private ModelAndView solicitudes(List<Solicitud> solicitudes) {
		ModelAndView result = new ModelAndView();
		
		if(solicitudes == null || solicitudes.isEmpty()){
			List<Solicitud> allSolicitudes = (ArrayList<Solicitud>) solicitudService.getAllSolicitudes();
			solicitudes = allSolicitudes;
		} 
			
		result.setViewName("solicitudes");
		result.addObject("solicitudes", solicitudes);
		return result;
	}
}
