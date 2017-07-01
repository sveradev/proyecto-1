package com.talgham.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String login(@RequestParam(value="username", required=false, defaultValue="") String username, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("password", "");
        return "login";
    }

}