package com.talgham.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity

public class Permiso {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Date fechaCreacion;
	private Date fechaModificado;
	private Date fechaFinalizado;
	@ManyToOne @JoinColumn(name="actividad_id")
	private Actividad actividad;
	private String permiso;
	@ManyToOne @JoinColumn(name="usuario_id")
	private Usuario usuario;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
	public String getPermiso() {
		return permiso;
	}
	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}
	public void setFechaFinalizado(Date fechaFinalizado) {
		this.fechaFinalizado = fechaFinalizado;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
