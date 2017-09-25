package com.talgham.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Email;
import com.talgham.demo.model.Programa;
import com.talgham.demo.model.Solicitud;

@Component
public class TareaService {

	@Autowired
	private ProgramaService programaService;
	@Autowired
	private SolicitudService solicitudService;
	@Autowired
	private EmailService emailService;
	
	private static final Logger log = LoggerFactory.getLogger(TareaService.class);
	
	private static final SimpleDateFormat formatterTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	
	@Scheduled(fixedRate = 24*60*60*1000)
	public void crearSolicitudProgramada() {
		log.info("Inicio de creacion de solicitudes programadas - {}", formatterTime.format(new Date()));

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		Date desde = c.getTime();

		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date hasta = c2.getTime();
		List<Programa> programas = (List<Programa>) programaService.buscarActivosPorFechaProximo(desde,hasta);
		for(Programa programa : programas) {
			Solicitud solicitud = new Solicitud();
			solicitud.setTrabajo(programa.getTrabajo());
			solicitud.setCliente(programa.getCliente());
			solicitud.setDescripcion(programa.getTrabajo().getDescripcion());
			solicitud.setFechaSolicitado(programa.getFechaProximo());
			solicitud.setProgramada(Boolean.TRUE);
			if(!Constantes.GUARDADO.equalsIgnoreCase(solicitudService.addSolicitud(solicitud))){
				log.error("Ha ocurrido un error al crear la solicitud para el programa con ID: {}", programa.getId());
				break;
			}
			log.info("Se creo la solicitud {} exitosamente",solicitud.getId());
			Integer incremento;
			switch (programa.getPeriodicidad()) {
				case Constantes.PERIODICIDAD_MENSUAL: incremento = 1;
				case Constantes.PERIODICIDAD_BIMESTRAL: incremento = 2;
				case Constantes.PERIODICIDAD_TRIMESTRAL: incremento = 3;
				case Constantes.PERIODICIDAD_SEMESTRAL: incremento = 6;
				case Constantes.PERIODICIDAD_ANUAL: incremento = 12;
				default : incremento = 1;
			}
			
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(programa.getFechaProximo()); 
			cal.add(Calendar.MONTH, incremento );
			programa.setFechaProximo(cal.getTime());
			programa.setFechaUltimo(solicitud.getFechaSolicitado());
			programaService.guardar(programa);
		}
		log.info("Finalizó la creacion de solicitudes programadas - {}", formatterTime.format(new Date()));
	}
	
	@Scheduled(fixedRate = 7*24*60*60*1000)
	public void enviarSolicitudReporte() throws ParseException {
		
		Calendar c = Calendar.getInstance();
		if(c.DAY_OF_MONTH != 1){
			return;
		}
		c.set(Calendar.DAY_OF_MONTH, 1);
		Date fechaDesde = c.getTime();

		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date fechaHasta = c2.getTime();
		
		List<Solicitud> solicitudes = (List<Solicitud>) solicitudService.buscarPorFechas(fechaDesde, fechaHasta);
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
				log.error("Ha ocurrido un error al enviar el email del reporte de solicitudes.");
			}
		}
	}
}
