package com.talgham.demo.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Solicitud {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nombre;
	private String titulo;
	private String email;
	private String descripcion;
	private Date fechaSolicitado;
	private Date fechaModificado;
	private Date fechaFinalizado;
	private String estado;
	private Calendar calendario = new GregorianCalendar();

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
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Boolean IsSuccess() {
		calendario.add(Calendar.DATE, 7);
		return estado.equalsIgnoreCase("SOLICITADO") && calendario.before(fechaSolicitado);
	}
	public Boolean IsPending() {
		calendario.add(Calendar.DATE, 7);
		return estado.equalsIgnoreCase("SOLICITADO") && calendario.after(fechaSolicitado);
	}
	public Boolean IsOverdue() {
		calendario.add(Calendar.DATE, 15);
		return estado.equalsIgnoreCase("SOLICITADO") && calendario.after(fechaSolicitado);
	}
}
