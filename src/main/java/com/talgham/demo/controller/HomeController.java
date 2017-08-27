package com.talgham.demo.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/home")
	public String home(Model model) {
		
		model.addAttribute("bienvenida", messageSource.getMessage("bienvenida.mensaje",new Object[]{},new Locale("")));
		model.addAttribute("textBody", messageSource.getMessage("textBoby",new Object[]{},new Locale("")));
		return "home";
	}
}