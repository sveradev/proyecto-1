package com.talgham.demo.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Programa;
import com.talgham.demo.model.Solicitud;

@Component
public class TareaService {

	@Autowired
	private ProgramaService programaService;
	@Autowired
	private SolicitudService solicitudService;
	
	
	private static final Logger log = LoggerFactory.getLogger(TareaService.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Scheduled(fixedRate = 24*60*60*1000)
	public void crearSolicitudProgramada() {
		log.info("Inicio de creacion de solicitudes programadas - {}", dateFormat.format(new Date()));

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
			switch (programa.getTrabajo().getPeriodicidad()) {
				case Constantes.PERIODICIDAD_MENSUAL: incremento = 1;
				case Constantes.PERIODICIDAD_BIMESTRAL: incremento = 2;
				case Constantes.PERIODICIDAD_TRIMESTRAL: incremento = 3;
				case Constantes.PERIODICIDAD_SEMESTRAL: incremento = 6;
				case Constantes.PERIODICIDAD_ANUAL: incremento = 12;
				default : incremento = 1;
			}
			
			Calendar cal = Calendar.getInstance(); 
            cal.setTime(programa.getFechaUltimo()); 
            cal.add(Calendar.MONTH, incremento );
			programa.setFechaProximo(cal.getTime());
			programa.setFechaUltimo(solicitud.getFechaSolicitado());
			programaService.guardar(programa);
		}
		log.info("Finalizó la creacion de solicitudes programadas - {}", dateFormat.format(new Date()));
	}
}
