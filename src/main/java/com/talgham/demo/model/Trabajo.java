package com.talgham.demo.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trabajo {
	
	@Id
  @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String nombre;
	private String descripcion;
	private Date fechaCreacion;
	private Date fechaBaja;

}
