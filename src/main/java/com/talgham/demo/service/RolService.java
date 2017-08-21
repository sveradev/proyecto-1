package com.talgham.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talgham.demo.model.Rol;
import com.talgham.demo.repository.RolRepository;

@Component
public class RolService {
	
	@Autowired
	private RolRepository rolRepository;

	public Iterable<Rol> getAllRoles() {
		return rolRepository.findAll();
	}
}