package com.talgham.demo.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Email;
import com.talgham.demo.model.Estado;
import com.talgham.demo.model.Perfil;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.model.Trabajo;
import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.EmailService;
import com.talgham.demo.service.EstadoService;
import com.talgham.demo.service.PerfilService;
import com.talgham.demo.service.SolicitudService;
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
	private MessageSource messageSource;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	

	@RequestMapping("/solicitudes")
	public String solicitudes(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuario);
		model.addAttribute("solicitudes", solicitudService.buscarSolicitudes(usuario));
		return "solicitudes";
	}
	
	@RequestMapping("/agenda")
	public String agenda(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuario);
		model.addAttribute("solicitudes", solicitudService.buscarAgenda(usuario));
		return "agenda";
	}
	
	@GetMapping("/crearSolicitud")
	public String crearSolicitud(Model model) {
		Perfil perfil = perfilService.buscarPorOrden(Constantes.PERFIL_CONTADOR);
		if(perfil != null){
			List<Usuario> responsables = (List<Usuario>) usuarioService.buscarPorPerfil(perfil.getId());
			if(responsables!= null && !responsables.isEmpty()){
				model.addAttribute("responsables",responsables);
			} else {
				model.addAttribute("tipoSalida",Constantes.ALERTA_DANGER);
				model.addAttribute("salida",messageSource.getMessage("solicitud.no.existe.responsables",new Object[]{},new Locale("")));
			}
		} else {
			model.addAttribute("tipoSalida", Constantes.ALERTA_DANGER);
			model.addAttribute("salida", messageSource.getMessage("solicitud.no.existe.perfiles",new Object[]{},new Locale("")));
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario",usuarioSession);
		return "crearSolicitud";
	}

	@PostMapping(path="/crearSolicitud")
	public @ResponseBody ModelAndView addSolicitud (
			@RequestParam String titulo,
			@RequestParam String descripcion) throws ParseException {
		
		ModelAndView result = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		Solicitud solicitud = new Solicitud();
		solicitud.setTitulo(titulo);
		solicitud.setDescripcion(descripcion);
		if(!Constantes.GUARDADO.equalsIgnoreCase(solicitudService.addSolicitud(solicitud,usuarioSession))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.guardada.error",new Object[]{},new Locale("")));
			result.addObject("usuario",usuarioSession);
			result.setViewName("solicitudes");
			return result;
		}
		result.addObject("solicitudes", solicitudService.getAllSolicitudes());
		
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
//				loggin "Hubo un error al enviar el mail.";
			}
//		} else {
//			loggin No se ha encontrado un email configurado para la acción requerida.
		}	
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("solicitud.creada.exito",new Object[]{solicitud.getId()},new Locale("")));
		result.addObject("usuario",usuarioSession);
		result.setViewName("solicitudes");
		return result;
	}
	
	@RequestMapping("/editarSolicitud")
	public ModelAndView editarSolicitud(@RequestParam(value="id") Long id) {
		ModelAndView result = new ModelAndView("editarSolicitud");
		
		Solicitud solicitud = solicitudService.buscarPorId(id);
		if(solicitud == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.encontrada",new Object[]{},new Locale("")));
			result.addObject("solicitudes", solicitudService.getAllSolicitudes());
			result.setViewName("solicitudes");
			return result;
		}
		result.addObject("solicitud", solicitud);
		String fechaSol = formatter.format(solicitud.getFechaSolicitado());
		result.addObject("fechaSol", fechaSol);
		
		List<Estado> estados = (List<Estado>) estadoService.getAllEstados();
		if(estados == null || estados.isEmpty()){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.existe.estados",new Object[]{},new Locale("")));
			return result;
		}
		result.addObject("estados",estados);
		Perfil perfil = perfilService.buscarPorOrden(Constantes.PERFIL_CONTADOR);
		if(perfil == null){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.existe.roles",new Object[]{},new Locale("")));
			return result;
		}
		List<Usuario> responsables = (List<Usuario>) usuarioService.buscarPorPerfil(perfil.getId());
		if(responsables == null || responsables.isEmpty()){
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
			@RequestParam Long tipoTrabajo,
			@RequestParam String email,
			@RequestParam String descripcion, 
			@RequestParam Long responsable,
			@RequestParam String fechaSol ) throws ParseException {
		
		Solicitud solicitud = solicitudService.buscarPorId(id);
		if(fechaSol!= null && !fechaSol.equalsIgnoreCase("")){
			Date fechaSolicitado = formatter.parse(fechaSol);
			solicitud.setFechaSolicitado(fechaSolicitado);
		}
		Trabajo trabajo = trabajoService.buscarPorId(tipoTrabajo);
		solicitud.setTrabajo(trabajo);
		solicitud.setDescripcion(descripcion);
		Estado estadoSeleccionado = estadoService.buscarPorId(estado);
		solicitud.setEstado(estadoSeleccionado);
		ModelAndView result = new ModelAndView("solicitudes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		result.addObject("usuario",usuarioSession);
		result.addObject("solicitudes", solicitudService.getAllSolicitudes());
		if(!Constantes.GUARDADO.equalsIgnoreCase(solicitudService.updateSolicitud(solicitud))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.guardada.error",new Object[]{solicitud.getId()},new Locale("")));
			return result;
		}
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida", messageSource.getMessage("solicitud.editada.exito",new Object[]{solicitud.getId()},new Locale("")));
		return result;
	}
	
	@PostMapping(path="/eliminarSolicitud")
	public @ResponseBody ModelAndView eliminarSolicitud (@RequestParam Long id) throws ParseException {
		
		Solicitud solicitud = solicitudService.buscarPorId(id);
		Estado estadoSeleccionado = estadoService.buscarPorOrden(Constantes.ESTADO_CANCELADO);
		solicitud.setEstado(estadoSeleccionado);
		
		ModelAndView result = new ModelAndView("solicitudes");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		result.addObject("usuario",usuarioService.buscarPorEmail(auth.getName()));
		if(!Constantes.GUARDADO.equalsIgnoreCase(solicitudService.updateSolicitud(solicitud))){
			result.addObject("tipoSalida",Constantes.ALERTA_DANGER);
			result.addObject("salida", messageSource.getMessage("solicitud.no.guardada.error",new Object[]{solicitud.getId()},new Locale("")));
			return result;
		}
		result.addObject("solicitudes", solicitudService.getAllSolicitudes());
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
			model.addAttribute("solicitudes",solicitudService.getAllSolicitudes());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
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
			result.addObject("solicitudes", solicitudService.getAllSolicitudes());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
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
				
				String templateCompilado = SolicitudController.getEmailTemplate(templateHtml, data);
				 
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
		
		result.addObject("solicitudes", solicitudService.getAllSolicitudes());
		result.addObject("tipoSalida",Constantes.ALERTA_SUCCESS);
		result.addObject("salida",messageSource.getMessage("email.reporte.enviado",new Object[]{},new Locale("")));
		return result;
	}
	
	public static String getEmailTemplate(String templateHtml, Map<String, Object> model) {
		MustacheFactory mf = new DefaultMustacheFactory();
		String result = null;
		FileReader fr;
		try {
			//lee el archivo html y lo trasforma a un string
			fr = new FileReader(templateHtml);
			BufferedReader br= new BufferedReader(fr);
			StringBuilder content=new StringBuilder(1024);
			String s = null;
			while((s=br.readLine())!=null) {
				content.append(s);
			}
			br.close();
			//crea un compilador de mopustache, necesita un stream de lectura del string que creamos antes (strinReader)
			Mustache template = mf.compile(new StringReader(content.toString()), content.toString());
			StringWriter writer = new StringWriter();
			
			//Ejecuta el template, con el stream de lectura, el de escitura y el modelo a popular
			template.execute(writer, model);
			writer.flush();
			result = writer.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//como resultado tenemos un lindo string con html populado
		return result;
	}
}
