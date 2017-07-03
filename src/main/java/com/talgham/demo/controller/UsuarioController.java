package com.talgham.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talgham.demo.service.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
    @RequestMapping("/usuario")
    public String usuario(Model model) {
        model.addAttribute("nombre", "");
        model.addAttribute("alias", "");
        model.addAttribute("email", "");
        model.addAttribute("password", "");
        return "usuario";
    }

	@PostMapping(path="/usuario")
	public @ResponseBody String createUsuario (@RequestParam String nombre,
			@RequestParam String alias,
			@RequestParam String email,
			@RequestParam String password) {

		usuarioService.createUsuario(nombre, alias,email,password);

		return "Saved";
	}

}