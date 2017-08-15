package com.talgham.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MensajeController {

	@GetMapping("/mensaje")
	public String mensaje(String mensaje, Model model) {
		model.addAttribute("mensaje", mensaje);
		return "mensaje";
	}
	
}
