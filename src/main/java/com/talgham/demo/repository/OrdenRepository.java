package com.talgham.demo.repository;

import org.springframework.data.repository.CrudRepository;
import com.talgham.demo.model.Orden;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface OrdenRepository extends CrudRepository<Orden, Long> {

}
