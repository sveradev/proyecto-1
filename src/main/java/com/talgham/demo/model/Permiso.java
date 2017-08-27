package com.talgham.demo.model;

import java.util.Date;

public class Permiso {
	
	private Long id;
	private Date fechaCreacion;
	private Date fechaModificado;
	private Date fechaFinalizado;
	private Actividad actividadNormalizado;
	private String permiso;
	private Usuario usuarioNormalizado;
	private String usuario;
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
	public void setFechaFinalizado(Date fechaFinalizado) {
		this.fechaFinalizado = fechaFinalizado;
	}
	public Actividad getActividadNormalizado() {
		return actividadNormalizado;
	}
	public void setActividadNormalizado(Actividad actividadNormalizado) {
		this.actividadNormalizado = actividadNormalizado;
	}
	public String getPermiso() {
		return permiso;
	}
	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}
	public Usuario getUsuarioNormalizado() {
		return usuarioNormalizado;
	}
	public void setUsuarioNormalizado(Usuario usuarioNormalizado) {
		this.usuarioNormalizado = usuarioNormalizado;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}