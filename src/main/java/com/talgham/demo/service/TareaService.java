package com.talgham.demo.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.talgham.demo.model.Programa;

@Component
public class TareaService {

	@Autowired
	private ProgramaService programaService;
	
	private static final Logger log = LoggerFactory.getLogger(TareaService.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	
	
	@Scheduled(fixedRate = 24*60*60*1000)
	public void crearSolicitudProgramada() {
		log.info("The time is now {}", dateFormat.format(new Date()));
	    //implementer

		Calendar c = Calendar.getInstance();   // this takes current date
		c.set(Calendar.DAY_OF_MONTH, 1);
	    Date desde = c.getTime();
		    
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date hasta = c2.getTime();
		List<Programa> programa = (List<Programa>) programaService.buscarPor(desde,hasta);
	}
}
