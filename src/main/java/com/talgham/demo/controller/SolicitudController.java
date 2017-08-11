package com.talgham.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talgham.demo.model.Solicitud;
import com.talgham.demo.repository.SolicitudRepository;
import com.talgham.demo.service.SolicitudService;

@Controller
public class SolicitudController {

	@Autowired
	private SolicitudService solicitudService;
	
    @RequestMapping("/crearSolicitud")
    public String solicitud(@RequestParam(value="numero", required=false, defaultValue="000001") String numero, Model model) {
        model.addAttribute("numero", numero);
        return "crearSolicitud";
    }


	@PostMapping(path="/crearSolicitud")
	public @ResponseBody String addSolicitud (@RequestParam String nombre,
			@RequestParam String titulo,
			@RequestParam String email,
			@RequestParam String descripcion) {

		solicitudService.addSolicitud(nombre, titulo, email, descripcion);
		
		return "Guardado";
	}
	@Autowired // This means to get the bean called solicitudRepository
	private SolicitudRepository solicitudRepository;

	@GetMapping(path="/solicitudes")
	public @ResponseBody Iterable<Solicitud> getAllSolicitudes() {
		return solicitudRepository.findAll();
	}
}