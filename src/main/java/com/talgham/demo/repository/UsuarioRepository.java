package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.talgham.demo.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Usuario findById(Long id);
	Usuario findByNombre(String nombre);
	Usuario findByEmail(String email);
	Iterable<Usuario> findByRol_id(Long id);
	Iterable<Usuario> findByPerfil_id(Long id);
	Iterable<Usuario> findByPerfil_orden(Integer orden);
}
