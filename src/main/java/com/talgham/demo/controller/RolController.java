
package com.talgham.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.talgham.demo.model.Rol;
import com.talgham.demo.service.RolService;

@Controller
public class RolController {

	@Autowired
	private RolService rolService;
	
	@RequestMapping("/roles")
	public String roles(@RequestParam(value="id", required=false, defaultValue="") String id, Model model) {

		ArrayList<Rol> roles = (ArrayList<Rol>) rolService.getAllRoles();
		model.addAttribute("roles", roles);
		return "roles";
	}
}
