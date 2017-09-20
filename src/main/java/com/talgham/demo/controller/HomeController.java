package com.talgham.demo.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.UsuarioService;

@Controller
public class HomeController {
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/")
	public String homes(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario", usuarioSession);
		model.addAttribute("bienvenida", messageSource.getMessage("bienvenida.mensaje",new Object[]{},new Locale("")));
		model.addAttribute("mensaje", messageSource.getMessage("mensaje",new Object[]{},new Locale("")));
		return "home";
	}
	
	@GetMapping("/home")
	public String home(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioSession = usuarioService.buscarPorEmail(auth.getName());
		model.addAttribute("usuario", usuarioSession);
		model.addAttribute("bienvenida", messageSource.getMessage("bienvenida.mensaje",new Object[]{},new Locale("")));
		model.addAttribute("mensaje", messageSource.getMessage("mensaje",new Object[]{},new Locale("")));
		return "home";
	}
}