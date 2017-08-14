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