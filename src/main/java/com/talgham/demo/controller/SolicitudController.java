package com.talgham.demo.controller;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talgham.demo.model.Solicitud;
import com.talgham.demo.repository.SolicitudRepository;

@Controller
public class SolicitudController {

    @RequestMapping("/solicitud")
    public String solicitud(@RequestParam(value="numero", required=false, defaultValue="000001") String numero, Model model) {
        model.addAttribute("numero", numero);
        return "solicitud";
    }

	@Autowired // This means to get the bean called solicitudRepository
	private SolicitudRepository solicitudRepository;

	@GetMapping(path="/addSolicitud")
	public @ResponseBody String addSolicitud (@RequestParam String nombre
			, @RequestParam String descripcion) {

		Solicitud n = new Solicitud();
		Date fechaSolicitado = new Date(Calendar.getInstance().getTime().getTime());
		String estado = "SOLICITADO";
		
		n.setNombre(nombre);
		n.setFechaSolicitado(fechaSolicitado);
		n.setEstado(estado);
		n.setDescripcion(descripcion);
		solicitudRepository.save(n);
		return "Saved";
	}

	@GetMapping(path="/allSolicitudes")
	public @ResponseBody Iterable<Solicitud> getAllSolicitudes() {
		return solicitudRepository.findAll();
	}
}