package com.talgham.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.common.Constantes;
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
	
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/crearSolicitud")
	public String crearSolicitud(Model model) {
		Rol rol = rolService.buscarPorOrden(Constantes.ROL_CONTADOR);
		if(rol != null){
			List<Usuario> responsables = (List<Usuario>) usuarioService.buscarPorRol(rol.getId());
			if(responsables!= null && !responsables.isEmpty()){
				model.addAttribute("responsables",responsables);
			} else {
				model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
				model.addAttribute("salida",messageSource.getMessage("solicitud.no.existe.responsables",new Object[]{},new Locale("")));
			}
		} else {
			model.addAttribute("tipoSalida", Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
		}
		return "crearSolicitud";
	}

	@PostMapping(path="/crearSolicitud")
	public @ResponseBody ModelAndView addSolicitud (
			@RequestParam String nombre,
			@RequestParam String titulo,
			@RequestParam String email,
			@RequestParam String descripcion,
			@RequestParam Long responsable,
			@RequestParam String fechaSol) throws ParseException {
		
		ModelAndView result = new ModelAndView("solicitudes");
		result.addObject("solicitudes", solicitudService.getAllSolicitudes());
		
		Solicitud solicitud = new Solicitud();
		solicitud.setNombre(nombre);
		solicitud.setTitulo(titulo);
		solicitud.setEmail(email);
		solicitud.setDescripcion(descripcion);
		Usuario usuario = usuarioService.buscarUsuarioPorId(responsable);
		solicitud.setResponsable(usuario);
		if(fechaSol!= null && !fechaSol.equalsIgnoreCase("")){
			Date fechaSolicitado = formatter.parse(fechaSol);
			solicitud.setFechaSolicitado(fechaSolicitado);
		}
		if(!Constantes.GUARDADO.equalsIgnoreCase(solicitudService.addSolicitud(solicitud)){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.creada.error",new Object[]{},new Locale("")));
			return result;
		}
		//Envio de mail.
		Email emailTemplate = emailService.buscarPorActividad(Constantes.ACTIVIDAD_CREAR_SOLICITUD);

		if(emailTemplate != null){
			String to = emailTemplate.getDireccion() + ";" + solicitud.getResponsable().getEmail();
			String subject = emailTemplate.getSubject();
			String texto = emailTemplate.getTexto();
		
			try {
				emailService.prepareAndSend(to, subject, texto);
				emailService.prepareAndSend(solicitud.getEmail(), subject, texto);
			} catch (Exception e) {
				e.printStackTrace();
//				loggin "Hubo un error al enviar el mail.";
			}
//		} else {
//			loggin No se ha encontrado un email configurado para la acción requerida.
		}		
		
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("solicitud.creada.exito",new Object[]{solicitud.getId()},new Locale("")));
		return result;
	}
	
	@RequestMapping("/editarSolicitud")
	public ModelAndView editarSolicitud(@RequestParam(value="id") Long id) {
		ModelAndView result = new ModelAndView();
		
		Solicitud solicitud = solicitudService.buscarPorId(id);
		if(solicitud == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.encontrada",new Object[]{},new Locale("")));
			result.addObject("solicitudes", solicitudService.getAllSolicitudes());
			result.setViewName("solicitudes");
			return result;
		}
		result.setViewName("editarSolicitud");
		result.addObject("solicitud", solicitud);
		String fechaSol = formatter.format(solicitud.getFechaSolicitado());
		result.addObject("fechaSol", fechaSol);
		
		List<Estado> estados = (List<Estado>) estadoService.getAllEstados();
		if(estados == null && estados.isEmpty()){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.existe.estados",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("estados",estados);
		Rol rol = rolService.buscarPorOrden(Constantes.ROL_CONTADOR);
		if(rol == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
			return result;
		}
		List<Usuario> responsables = (List<Usuario>) usuarioService.buscarPorRol(rol.getId());
		if(responsables == null && responsables.isEmpty()){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.existe.responsables",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("responsables",responsables);
		return result;
	}

	@PostMapping(path="/editarSolicitud")
	public @ResponseBody ModelAndView editarSolicitud (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam Long estado,
			@RequestParam String titulo,
			@RequestParam String email,
			@RequestParam String descripcion, 
			@RequestParam Long responsable,
			@RequestParam String fechaSol ) throws ParseException {
		
		Solicitud solicitud = new Solicitud();
		
		if(fechaSol!= null && !fechaSol.equalsIgnoreCase("")){
			Date fechaSolicitado = formatter.parse(fechaSol);
			solicitud.setFechaSolicitado(fechaSolicitado);
		}
		
		solicitud.setId(id);
		solicitud.setNombre(nombre);
		solicitud.setTitulo(titulo);
		solicitud.setEmail(email);
		solicitud.setDescripcion(descripcion);
		Usuario usuario = usuarioService.buscarUsuarioPorId(responsable);
		solicitud.setResponsable(usuario);
		Estado estadoSeleccionado = estadoService.buscarPorId(estado);
		solicitud.setEstado(estadoSeleccionado);
		solicitud.setFechaModificado(new Date());
		if(estadoSeleccionado!= null && estadoSeleccionado.getOrden() == Constantes.ESTADO_FINALIZADO){
			solicitud.setFechaFinalizado(new Date());
		}
		
		solicitudService.updateSolicitud(solicitud);
		
		ModelAndView result = solicitudes(null);
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("solicitud.editada.exito",new Object[]{solicitud.getId()},new Locale("")));
		
		return result;
	}
	
	@GetMapping("/buscarSolicitud")
	public String buscarSolicitud(Model model) {
		Rol rol = rolService.buscarPorOrden(Constantes.ROL_CONTADOR);
		if(rol != null){
			List<Usuario> responsables = (List<Usuario>) usuarioService.buscarPorRol(rol.getId());
			if(!responsables.isEmpty()){
				model.addAttribute("responsables",responsables);
			} else {
				model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
				model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.responsables",new Object[]{},new Locale("")));
			}
		} else {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
		}
		return "buscarSolicitud";
	}
	
	@PostMapping(path="/buscarSolicitud")
	public @ResponseBody ModelAndView postBuscarSolicitud (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String titulo,
			@RequestParam Long responsable,
			@RequestParam String fechaDesde,
			@RequestParam String fechaHasta) throws ParseException {
		
		Date solicitadoDesde = null;
		Date solicitadoHasta = null;
		
		if(fechaDesde!= null && !fechaDesde.equalsIgnoreCase("")){
			solicitadoDesde = formatter.parse(fechaDesde);
		}
		
		if(fechaHasta!= null && !fechaHasta.equalsIgnoreCase("")){
			solicitadoHasta = formatterTime.parse(fechaHasta+" 23:23:59");
		}
		
		List<Solicitud> solicitudes = new ArrayList<Solicitud>();
		String tipoSalida = "";
		String salida = "";
		
		if(id != null){
			Solicitud solicitud = solicitudService.buscarPorId(id);
			if(solicitud != null){
				solicitudes.add(solicitud);
				tipoSalida = Constantes.ALERTA_SUCCESS;
				salida = messageSource.getMessage("buscar.solicitud.exito",new Object[]{},new Locale(""));
			} else {
				tipoSalida = Constantes.ALERTA_WARNING;
				salida = messageSource.getMessage("buscar.solicitud.id.no.existe",new Object[]{id},new Locale(""));
			}
		} else {
			solicitudes = (List<Solicitud>) solicitudService.buscar(nombre,titulo,responsable,solicitadoDesde,solicitadoHasta);
			if(solicitudes == null){
				ModelAndView result = new ModelAndView();
				result.addObject("tipoSalida",Constantes.ALERTA_WARNING);
				result.addObject("salida",messageSource.getMessage("buscar.solicitud.completar.campo",new Object[]{},new Locale("")));
				result.setViewName("buscarSolicitud");
				buscarSolicitud((Model) result);
			} else if(solicitudes.isEmpty()){
				ModelAndView result = new ModelAndView();
				result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
				result.addObject("salida",messageSource.getMessage("buscar.solicitud.no.registros",new Object[]{},new Locale("")));
				result.setViewName("buscarSolicitud");
				buscarSolicitud((Model) result);
			} else {
				tipoSalida = Constantes.ALERTA_SUCCESS;
				salida = messageSource.getMessage("buscar.solicitudes.exito",new Object[]{},new Locale(""));
			}
		}
		ModelAndView result = this.solicitudes(solicitudes);
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
	
	
	@PostMapping(path="/enviarReporte")
	public @ResponseBody ModelAndView enviarReporte (
			@RequestParam String fechaDesde,
			@RequestParam String fechaHasta) throws ParseException {
		
		if(fechaDesde!= null && !fechaDesde.equalsIgnoreCase("") && fechaHasta!= null && !fechaHasta.equalsIgnoreCase("")){
			Date solicitadoDesde = formatter.parse(fechaDesde);
			Date solicitadoHasta = formatterTime.parse(fechaHasta+" 23:23:59");
			List<Solicitud> solicitudes = (List<Solicitud>) solicitudService.buscarPorFechas(solicitadoDesde, solicitadoHasta);
			//Envio de mail.
			Email emailTemplate = emailService.buscarPorActividad(Constantes.ACTIVIDAD_REPOTAR_SOLICITUD);

			if(emailTemplate != null){
				String to = emailTemplate.getDireccion();
				String subject = emailTemplate.getSubject();
				String texto = emailTemplate.getTexto();//falta template Email.
			
				try {
					emailService.prepareAndSend(to, subject, texto);
				} catch (Exception e) {
					e.printStackTrace();
					ModelAndView result = this.solicitudes(null);
					result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
					result.addObject("salida",messageSource.getMessage("email.reporte.no.enviado",new Object[]{},new Locale("")));
					result.setViewName("solicitudes");
					return result;
//					loggin "Hubo un error al enviar el mail.";
				}
			} else {
				ModelAndView result = this.solicitudes(null);
				result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
				result.addObject("salida",messageSource.getMessage("email.actividad.no.encontrado",new Object[]{},new Locale("")));
				result.setViewName("solicitudes");
				return result;
//				loggin No se ha encontrado un email configurado para la acción requerida.
			}
		}
		
		ModelAndView result = this.solicitudes(null);
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida",messageSource.getMessage("email.reporte.enviado",new Object[]{},new Locale("")));
		result.setViewName("solicitudes");
		return result;
	}
}
