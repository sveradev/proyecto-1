package com.talgham.demo.model;

import java.sql.Date;

import com.talgham.demo.repository.ActividadRepository;

public class Actividad {
	
	ActividadRepository actividadRepository;
	
	private long id;
	private String nombre;
	private String descripcion;
	private Date fechaCreacion;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
