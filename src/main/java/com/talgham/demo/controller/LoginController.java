package com.talgham.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.model.Usuario;
import com.talgham.demo.service.UsuarioService;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(value="username", required=false, defaultValue="") String username, Model model) {
		model.addAttribute("username", username);
		model.addAttribute("password", "");
		model.addAttribute("salida", "");
		return "login";
	}

//	@Autowired 
//	private UsuarioService usuarioService;
//	
//	@PostMapping(path="/checkUser")
//	public ModelAndView login(@ModelAttribute Usuario usuario) {
//		
//		ModelAndView model = new ModelAndView("login");
//		model.addObject("usuario", usuario);
//		String alias = usuario.getAlias();
//
//		String salida="Usuario inexistente";
//		ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usuarioService.getAllUsuarios();
//		if(!usuarios.isEmpty()){
//			for (Usuario myUsuario : usuarios){
//				if(myUsuario.getAlias().equalsIgnoreCase(alias)){
//					if(myUsuario.getPassword().equalsIgnoreCase(usuario.getPassword())){
//							salida = "Usuario Valido";	
//							model = new ModelAndView("solicitud");
//					} else {
//						salida = "Password Incorrecto";
//					}
//				}
//			}
//		}
//		
//		model.addObject("salida", salida);
//		return model;
//	}
}