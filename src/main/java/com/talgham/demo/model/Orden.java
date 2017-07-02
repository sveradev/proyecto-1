package com.talgham.demo.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Orden {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private long idSolicitud;
    private String nombre;
    private String descripcion;
    private Date fechaSolicitado;
    private Date fechaRespuesta;
    private Date fechaFinalizado;
    private String estado;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
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
	public Date getFechaSolicitado() {
		return fechaSolicitado;
	}
	public void setFechaSolicitado(Date fechaSolicitado) {
		this.fechaSolicitado = fechaSolicitado;
	}
	public Date getFechaRespuesta() {
		return fechaRespuesta;
	}
	public void setFechaRespuesta(Date fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}
	public Date getFechaFinalizado() {
		return fechaFinalizado;
	}
	public void setFechaFinalizado(Date fechaFinalizado) {
		this.fechaFinalizado = fechaFinalizado;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
