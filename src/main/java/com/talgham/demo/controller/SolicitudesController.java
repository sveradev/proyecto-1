package com.talgham.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.talgham.demo.model.Solicitud;
import com.talgham.demo.service.SolicitudService;

@Controller
public class SolicitudesController {

	@Autowired
	SolicitudService solicitudService;

	@RequestMapping("/solicitudes")
	public String solicitudes(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {

		ArrayList<Solicitud> solicitudes = (ArrayList<Solicitud>) solicitudService.getAllSolicitudes();
		model.addAttribute("solicitudes", solicitudes);
//		for(Solicitud mySolicitud : solicitudes){
//			if(id.equalsIgnoreCase("") || mySolicitud.getId() == Long.parseLong(id)){
//				model.addAttribute("id", mySolicitud.getId());
//				model.addAttribute("nombre", mySolicitud.getNombre());
//				model.addAttribute("titulo", mySolicitud.getTitulo());
//				model.addAttribute("email", mySolicitud.getEmail());
//				model.addAttribute("estado", mySolicitud.getEstado());
//				model.addAttribute("descripcion", mySolicitud.getDescripcion());
//			}
//		}
//		
		return "solicitudes";
	}
}