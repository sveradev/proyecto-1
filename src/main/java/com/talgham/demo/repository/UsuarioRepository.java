package com.talgham.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.talgham.demo.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Usuario findByNombre(String nombre);
	Usuario findByEmail(String email);
	List<Usuario> findByRol(Long idRol);
}
