package com.talgham.demo.model;

import java.util.Date;

import com.talgham.demo.common.Constantes;

public class Solicitud {
	
	private Long id;
	private String nombre;
	private String titulo;
	private String email;
	private String descripcion;
	private Date fechaSolicitado;
	private Date fechaModificado;
	private Date fechaFinalizado;
	private Estado estado;
	private Usuario responsable;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Date getFechaModificado() {
		return fechaModificado;
	}
	public void setFechaModificado(Date fechaModificado) {
		this.fechaModificado = fechaModificado;
	}
	public Date getFechaFinalizado() {
		return fechaFinalizado;
	}
	public void setFechaFinalizado(Date fechaFinalizado) {
		this.fechaFinalizado = fechaFinalizado;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public Usuario getResponsable() {
		return responsable;
	}
	public void setResponsable(Usuario responsable) {
		this.responsable = responsable;
	}

	private Date addDays (Date date,Integer days){
		Long result = date.getTime() + (1000 * 60 * 60 * 24 * days);
		return new Date(result);
	}
	
	public Boolean IsSuccess() {
		return estado.getId() == Constantes.ESTADO_SOLICITADO && new Date().before(addDays(fechaSolicitado, 7));
	}
	public Boolean IsPending() {
		return estado.getId() == Constantes.ESTADO_SOLICITADO && new Date().after(addDays(fechaSolicitado, 7));
	}
	public Boolean IsOverdue() {
		return estado.getId() == Constantes.ESTADO_SOLICITADO && new Date().after(addDays(fechaSolicitado, 15));
	}
	public Boolean IsActive() {
		return estado.getId() != Constantes.ESTADO_SOLICITADO && estado.getId() != Constantes.ESTADO_FINALIZADO;
	}
}
