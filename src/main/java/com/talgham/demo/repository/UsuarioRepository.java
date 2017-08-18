package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;
import com.talgham.demo.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Usuario findByNombre(String nombre);
	Usuario findByEmail(String email);
}
