package com.talgham.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("login");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/crearSolicitud").setViewName("crearSolicitud");
		registry.addViewController("/editarSolicitud").setViewName("editarSolicitud");
		registry.addViewController("/seguimiento").setViewName("seguimiento");
		registry.addViewController("/solicitudes").setViewName("solicitudes");
	}
}