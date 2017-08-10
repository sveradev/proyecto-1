package com.talgham.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.talgham.demo.model.Greeting;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String greetingForm(Model model) {
        model.addAttribute("home", new Greeting());
        return "home";
    }    
}