
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
		Long idRol = new Long(3);
		Rol rol = rolService.buscarRolesPorId(idRol);
		if(rol != null){
			ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(rol.getNombre());
			if(!responsables.isEmpty()){
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
			@RequestParam String fechaSol
			) throws ParseException {
		
		Date fechaSolicitado = new Date();
		
		if(fechaSol!= null && !fechaSol.equalsIgnoreCase("")){
			fechaSolicitado = formatter.parse(fechaSol);
		}
		
		Solicitud solicitud = new Solicitud();
		solicitud.setNombre(nombre);
		solicitud.setTitulo(titulo);
		solicitud.setEmail(email);
		solicitud.setDescripcion(descripcion);
		Usuario usuario = usuarioService.buscarUsuarioPorId(responsable);
		solicitud.setResponsable(usuario);
		solicitud.setFechaSolicitado(fechaSolicitado);
		solicitudService.addSolicitud(solicitud);
		
		//Envio de mail.
		Email emailTemplate = emailService.buscarPorActividad(Constantes.ACTIVIDAD_CREAR_SOLICITUD);

		if(emailTemplate != null){
			String to = emailTemplate.getDireccion() + ";" + solicitud.getResponsable().getEmail();
			String subject = emailTemplate.getSubject();
			String texto = emailTemplate.getTexto();
		
			try {
				emailService.sendEmail(to, subject, texto);
				emailService.sendEmail(solicitud.getEmail(), subject, texto);
			} catch (Exception e) {
				e.printStackTrace();
//				loggin "Hubo un error al enviar el mail.";
			}
//		} else {
//			loggin No se ha encontrado un email configurado para la acción requerida.
		}		
		
		ModelAndView result = solicitudes(null);
		
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("solicitud.creada.exito",new Object[]{solicitud.getId()},new Locale("")));
		
		return result;
	}
	
	@RequestMapping("/editarSolicitud")
	public ModelAndView editarSolicitud(@RequestParam(value="id") Long id, Model model) {
		ModelAndView result = solicitudes(null);
		Solicitud solicitud = solicitudService.buscarPorId(id);
		if(solicitud !=null){
			Date date = solicitud.getFechaSolicitado();
			String fechaSol = formatter.format(date);
			model.addAttribute("fechaSol", fechaSol);
			model.addAttribute("solicitud", solicitud);
			ArrayList<Estado> estados = (ArrayList<Estado>) estadoService.getAllEstados();
			Integer rolOrden = Constantes.ROL_CONTADOR;
			Rol rol = rolService.buscarPorOrden(rolOrden);
			ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(rol.getNombre());
			model.addAttribute("responsables",responsables);
			model.addAttribute("estados",estados);
			result.setViewName("editarSolicitud");
		} else {
			List<Solicitud> solicitudes = (List<Solicitud>) solicitudService.getAllSolicitudes();
			if(solicitudes !=null && !solicitudes.isEmpty()){
				result.addObject("solicitudes",solicitudes);
//			} else {
			}
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.encontrada",new Object[]{},new Locale("")));
			result.setViewName("solicitudes");
		}
		
		return result;
	}

	@PostMapping(path="/editarSolicitud")
	public @ResponseBody ModelAndView editarSolicitud (@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam Integer estado,
			@RequestParam String titulo,
			@RequestParam String email,
			@RequestParam String descripcion, 
			@RequestParam Long responsable,
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
		Usuario usuario = usuarioService.buscarUsuarioPorId(responsable);
		solicitud.setResponsable(usuario);
		Estado estadoSeleccionado = estadoService.buscarPorId(estado);
		solicitud.setEstado(estadoSeleccionado);
		solicitud.setFechaSolicitado(fechaSolicitado);
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
		Integer orden = Constantes.ROL_CONTADOR;
		Rol rol = rolService.buscarPorOrden(orden);
		if(rol != null){
			ArrayList<Usuario> responsables = (ArrayList<Usuario>) usuarioService.buscarUsuariosPorRol(rol.getNombre());
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
					emailService.sendEmail(to, subject, texto);
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

	//Metodos privados
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
