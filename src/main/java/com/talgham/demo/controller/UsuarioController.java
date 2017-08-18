package com.talgham.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.service.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
    @RequestMapping("/crearUsuario")
    public String usuario(Model model) {
        model.addAttribute("nombre", "");
        model.addAttribute("alias", "");
        model.addAttribute("email", "");
        model.addAttribute("password", "");
        model.addAttribute("showModal", Boolean.FALSE);
        model.addAttribute("msgModalSalida", "");
        return "crearUsuario";
    }

	@PostMapping(path="/crearUsuario")
	public @ResponseBody ModelAndView createUsuario (@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam String password) {

		String response = usuarioService.crearUsuario(nombre, alias,email,password);
		ModelAndView model = new ModelAndView("crearUsuario");

		if(response.equalsIgnoreCase("saved")){
			model.addObject("showModal", Boolean.TRUE);
			model.addObject("msgModalSalida", "Guadado");
		}

		return model;
	}

}