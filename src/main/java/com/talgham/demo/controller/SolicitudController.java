package com.talgham.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Cliente;
import com.talgham.demo.model.Email;
import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Perfil;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.ClienteService;
import com.talgham.demo.service.EmailService;
import com.talgham.demo.service.EstadoService;
import com.talgham.demo.service.PerfilService;
import com.talgham.demo.service.SolicitudService;
import com.talgham.demo.service.TareaService;
import com.talgham.demo.service.TrabajoService;
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
	private PerfilService perfilService;
	@Autowired
	private TrabajoService trabajoService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private MessageSource messageSource;
	
	private static final Logger log = LoggerFactory.getLogger(TareaService.class);
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	

	@RequestMapping("/solicitudes")
	public String solicitudes(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuario);
		model.addAttribute("solicitudes", solicitudService.buscarSolicitudes(usuario, Boolean.FALSE));
		return "solicitudes";
	}
	
	@RequestMapping("/agenda")
	public String agenda(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuario);
		model.addAttribute("solicitudes", solicitudService.buscarSolicitudes(usuario, Boolean.TRUE));
		
		return "agenda";
	}
	
	@GetMapping("/crearSolicitud")
	public String crearSolicitud(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuarioSession);
		if(usuarioSession.isAdmin()) {
			model.addAttribute("clientes",clienteService.buscarClientes());
		}
		return "crearSolicitud";
	}

	@PostMapping(path="/crearSolicitud")
	public @ResponseBody ModelAndView addSolicitud (
			@RequestParam String titulo,
			@RequestParam String descripcion) throws ParseException {
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());

		ModelAndView result = new ModelAndView("solicitudes");
		Solicitud solicitud = new Solicitud();
		Cliente clienteSel = null;
			if(usuarioSession.isCliente()) {
				clienteSel = clienteService.buscarPorRepresentante(usuarioSession.getId());
			}
			if(usuarioSession.isContador()) {
				clienteSel = clienteService.buscarPorContador(usuarioSession.getId());
			}
		
		solicitud.setCliente(clienteSel);
		solicitud.setProgramada(Boolean.FALSE);
		solicitud.setTitulo(titulo);
		solicitud.setDescripcion(descripcion);
		
		if(!Constantes.GUARDADO.equalsIgnoreCase(solicitudService.addSolicitud(solicitud))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.guardada.error",new Object[]{},new Locale("")));
			result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			result.addObject("usuario",usuarioSession);
			return result;
		}
		result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
		
		//Envio de mail.
		Email emailTemplate = emailService.buscarPorActividad(Constantes.ACTIVIDAD_CREAR_SOLICITUD);

		if(emailTemplate != null){
			String to = emailTemplate.getDireccion();
			String subject = emailTemplate.getSubject();
			String texto = emailTemplate.getTexto();
		
			try {
				emailService.sendEmail(to, subject, texto);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Hubo un error al enviar el mail para {}.", to);
			}
			
			try {
				emailService.sendEmail(clienteSel.getRepresentante().getEmail(), subject, texto);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Hubo un error al enviar el mail para {}.", clienteSel.getRepresentante().getEmail());
			}
			
			try {
				emailService.sendEmail(clienteSel.getContador().getEmail(), subject, texto);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Hubo un error al enviar el mail para {}.", clienteSel.getContador().getEmail());
			}
		} else {
			log.error("No se ha encontrado un email configurado para la acción requerida.");
		}	
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("solicitud.creada.exito",new Object[]{solicitud.getId()},new Locale("")));
		result.addObject("usuario",usuarioSession);
		return result;
	}
	
	@RequestMapping("/editarSolicitud")
	public ModelAndView editarSolicitud(@RequestParam(value="id") Long id) {
		ModelAndView result = new ModelAndView("editarSolicitud");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario", usuarioSession);
		if(usuarioSession.isCliente()) {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.editar.sin.permiso",new Object[]{},new Locale("")));
			result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			result.setViewName("solicitudes");
			return result;
		}
		Solicitud solicitud = solicitudService.buscarPorId(id);
		if(solicitud == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.encontrada",new Object[]{},new Locale("")));
			result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			result.setViewName("solicitudes");
			return result;
		}
		if(usuarioSession.isAdmin()) {
			result.addObject("clientes",clienteService.buscarClientes());
		} 
		result.addObject("solicitud", solicitud);
		result.addObject("fechaSol", formatter.format(solicitud.getFechaSolicitado()));
		
		List<Estado> estados = (List<Estado>) estadoService.getAllEstados();
		if(estados == null || estados.isEmpty()){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.existe.estados",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("estados",estados);
		result.addObject("trabajos", trabajoService.buscarTrabajos());
		return result;
	}

	@PostMapping(path="/editarSolicitud")
	public @ResponseBody ModelAndView editarSolicitud (@RequestParam Long id,
			@RequestParam String titulo,
			@RequestParam Long estado,
			@RequestParam Long trabajo,
			@RequestParam String descripcion, 
			@RequestParam String fechaSol) throws ParseException {
		
		ModelAndView result = new ModelAndView("solicitudes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		if(usuarioSession.isCliente()) {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.editar.sin.permiso",new Object[]{},new Locale("")));
			result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			result.setViewName("solicitudes");
			return result;
		}
		Solicitud solicitud = solicitudService.buscarPorId(id);
		if(fechaSol!= null && !fechaSol.equalsIgnoreCase("")){
			solicitud.setFechaSolicitado(formatter.parse(fechaSol));
		}
		solicitud.setTitulo(titulo);
		solicitud.setTrabajo(trabajoService.buscarPorId(trabajo));
		solicitud.setEstado(estadoService.buscarPorId(estado));
		solicitud.setDescripcion(descripcion);
		if(!Constantes.GUARDADO.equalsIgnoreCase(solicitudService.guardarSolicitud(solicitud))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.guardada.error",new Object[]{solicitud.getId()},new Locale("")));
			result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			return result;
		}
		result.addObject("usuario",usuarioSession);
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("solicitud.editada.exito",new Object[]{solicitud.getId()},new Locale("")));
		result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
		return result;
	}
	
	@RequestMapping(path="/eliminarSolicitud")
	public @ResponseBody ModelAndView eliminarSolicitud(@RequestParam Long id) throws ParseException {
		
		ModelAndView result = new ModelAndView("solicitudes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		if(usuarioSession.isCliente() && usuarioSession.isContador()) {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.editar.sin.permiso",new Object[]{},new Locale("")));
			result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			return result;
		}
		Solicitud solicitud = solicitudService.buscarPorId(id);
		Estado estadoSeleccionado = estadoService.buscarPorOrden(Constantes.ESTADO_CANCELADO);
		solicitud.setEstado(estadoSeleccionado);
		
		if(!Constantes.GUARDADO.equalsIgnoreCase(solicitudService.guardarSolicitud(solicitud))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.guardada.error",new Object[]{solicitud.getId()},new Locale("")));
			result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			return result;
		}
		result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("solicitud.editada.exito",new Object[]{solicitud.getId()},new Locale("")));
		return result;
	}
	
	@GetMapping("/buscarSolicitud")
	public String buscarSolicitud(Model model) {
		Perfil perfil = perfilService.buscarPorOrden(Constantes.PERFIL_CONTADOR);
		if(perfil == null){
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
			model.addAttribute("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			model.addAttribute("usuario",usuarioSession);
			return "solicitudes";
		}
		List<Usuario> responsables = (List<Usuario>) usuarioService.buscarPorPerfil(perfil.getId());
		if(!responsables.isEmpty()){
			model.addAttribute("responsables",responsables);
		} else {
			model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.responsables",new Object[]{},new Locale("")));
		}
		return "buscarSolicitud";
	}
		   
	private ModelAndView CargarCombosBusqueda (ModelAndView result) {
		if(result == null){
			result = new ModelAndView();
		}
		Perfil perfil = perfilService.buscarPorOrden(Constantes.PERFIL_CONTADOR);
		if(perfil == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
			result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
			result.addObject("usuario",usuarioSession);
			result.setViewName("solicitudes");
			return result;
		}
		List<Usuario> responsables = (List<Usuario>) usuarioService.buscarPorRol(perfil.getId());
		if(!responsables.isEmpty()){
			result.addObject("responsables",responsables);
			result.setViewName("buscarSolicitud");
			return result;
		} else {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.existe.responsables",new Object[]{},new Locale("")));
			result.setViewName("buscarSolicitud");
			return result;
		}
	}
	
	@PostMapping(path="/buscarSolicitud")
	public @ResponseBody ModelAndView postBuscarSolicitud (@RequestParam Long id,
			@RequestParam String titulo,
			@RequestParam String fechaDesde,
			@RequestParam String fechaHasta) throws ParseException {
		
		if(id != null){
			ModelAndView result = new ModelAndView ();
			List<Solicitud> solicitudes = new ArrayList<Solicitud>();
			Solicitud solicitud = solicitudService.buscarPorId(id);
			if(solicitud != null){
				solicitudes.add(solicitud);
				result.addObject(solicitudes);
				result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
				result.addObject("salida",messageSource.getMessage("buscar.solicitud.exito",new Object[]{id},new Locale("")));
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
				result.addObject("usuario",usuarioSession);
				result.setViewName("solicitudes");
				return result;
			} 
			result.addObject("tipoSalida",Constantes.ALERTA_WARNING);
			result.addObject("salida",messageSource.getMessage("buscar.solicitud.id.no.existe",new Object[]{id},new Locale("")));
			result.setViewName("buscarSolicitud");
			return CargarCombosBusqueda(result);
		}
		if(fechaDesde!= null && !fechaDesde.equalsIgnoreCase("") && fechaHasta!= null && !fechaHasta.equalsIgnoreCase("")){
			Date solicitadoDesde = formatter.parse(fechaDesde);
			Date solicitadoHasta = formatterTime.parse(fechaHasta+" 23:23:59");
			List<Solicitud> solicitudes = (List<Solicitud>) solicitudService.buscar(solicitadoDesde,solicitadoHasta);
			if(solicitudes!= null && !solicitudes.isEmpty()){
				ModelAndView result = new ModelAndView("solicitudes");
				result.addObject("solicitudes",solicitudService.getAllSolicitudes());
				result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
				result.addObject("salida",messageSource.getMessage("buscar.solicitudes.exito",new Object[]{},new Locale("")));
				return result;
			} else {
				ModelAndView result = new ModelAndView("buscarSolicitud");
				result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
				result.addObject("salida",messageSource.getMessage("buscar.solicitud.no.registros",new Object[]{},new Locale("")));
				return CargarCombosBusqueda(result);
			}
		}
		ModelAndView result = new ModelAndView ("buscarSolicitud");
		result.addObject("tipoSalida",Constantes.ALERTA_WARNING);
		result.addObject("salida",messageSource.getMessage("buscar.solicitud.completar.campo",new Object[]{},new Locale("")));
		return CargarCombosBusqueda(result);
	}

	@PostMapping(path="/enviarReporte")
	public @ResponseBody ModelAndView enviarReporte (
			@RequestParam String fechaDesde,
			@RequestParam String fechaHasta) throws ParseException {
		
		ModelAndView result = new ModelAndView("solicitudes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		
		if(fechaDesde!= null && !fechaDesde.equalsIgnoreCase("") && fechaHasta!= null && !fechaHasta.equalsIgnoreCase("")){
			Date solicitadoDesde = formatter.parse(fechaDesde);
			Date solicitadoHasta = formatterTime.parse(fechaHasta+" 23:23:59");
			
			List<Solicitud> solicitudes = (List<Solicitud>) solicitudService.buscarPorFechas(solicitadoDesde, solicitadoHasta);
			ModelAndView emailResult = new ModelAndView("EmailReporte");
			emailResult.addObject("solicitudes",solicitudes);

			//Envio de mail.
			Email emailTemplate = emailService.buscarPorActividad(Constantes.ACTIVIDAD_REPOTAR_SOLICITUD);

			if(emailTemplate != null){
				String to = emailTemplate.getDireccion();
				String subject = emailTemplate.getSubject();
			
				//Ruta completa al template html
				String templateHtml = "src/main/resources/emailTemplates/emailReporte.html";
				
				//Mapa de objetos a renderizar (clave elemento mustache, valor opbejto)
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("solicitudes", solicitudes);
				
				String templateCompilado = EmailService.getEmailTemplate(templateHtml, data);
				 
				try {
					emailService.sendEmail(to, subject, templateCompilado);
				} catch (Exception e) {
					e.printStackTrace();
					result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
					result.addObject("salida",messageSource.getMessage("email.reporte.no.enviado",new Object[]{},new Locale("")));
					return result;
				}
			} else {
				result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
				result.addObject("salida",messageSource.getMessage("email.actividad.no.encontrado",new Object[]{},new Locale("")));
				return result;
			}
		} else {
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida",messageSource.getMessage("solicitud.no.completa.fechas",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("solicitudes", solicitudService.buscarSolicitudes(usuarioSession, Boolean.FALSE));
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida",messageSource.getMessage("email.reporte.enviado",new Object[]{},new Locale("")));
		return result;
	}
}	