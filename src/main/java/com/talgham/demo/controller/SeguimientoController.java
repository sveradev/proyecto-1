package com.talgham.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SeguimientoController {

    @RequestMapping("/seguimiento")
    public String seguimiento(@RequestParam(value="numero", required=false, defaultValue="000001") String numero, Model model) {
        model.addAttribute("numero", numero);
        String estado = "INICIADO";
        model.addAttribute("estado", estado);
        String descripcion = "---";
        model.addAttribute("descripcion", descripcion);
        return "seguimiento";
    }

}