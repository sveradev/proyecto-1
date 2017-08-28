
package com.talgham.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.talgham.demo.model.Estado;
import com.talgham.demo.service.EstadoService;

@Controller
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	
	@RequestMapping("/estados")
	public String estados(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {

		ArrayList<Estado> estados = (ArrayList<Estado>) estadoService.getAllEstados();
		model.addAttribute("estados", estados);
		return "estados";
	}
}
