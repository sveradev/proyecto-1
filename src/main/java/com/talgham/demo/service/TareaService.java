package com.talgham.demo.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TareaService {

	private static final Logger log = LoggerFactory.getLogger(TareaService.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Scheduled(fixedRate = 24*60*60*1000)
	public void crearSolicitudProgramada() {
		log.info("The time is now {}", dateFormat.format(new Date()));
	    //implementer
	}
}
