package com.talgham.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Trabajo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nombre;
	private String tipo;
	private String email;
	private String descripcion;
	private String frecuencia;
	private Boolean programado;
	private Integer vencimiento;
	private Date fechaProgramado;
	private Date fechaModificado;
	private Date fechaApagado;
	private Date fechaUltimoCreado;
	@ManyToOne @JoinColumn(name="usuario_id")
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	public Boolean getProgramado() {
		return programado;
	}

	public void setProgramado(Boolean programado) {
		this.programado = programado;
	}

	public Integer getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Integer vencimiento) {
		this.vencimiento = vencimiento;
	}

	public Date getFechaProgramado() {
		return fechaProgramado;
	}

	public void setFechaProgramado(Date fechaProgramado) {
		this.fechaProgramado = fechaProgramado;
	}

	public Date getFechaModificado() {
		return fechaModificado;
	}

	public void setFechaModificado(Date fechaModificado) {
		this.fechaModificado = fechaModificado;
	}

	public Date getFechaApagado() {
		return fechaApagado;
	}

	public void setFechaApagado(Date fechaApagado) {
		this.fechaApagado = fechaApagado;
	}

	public Date getFechaUltimoCreado() {
		return fechaUltimoCreado;
	}

	public void setFechaUltimoCreado(Date fechaUltimoCreado) {
		this.fechaUltimoCreado = fechaUltimoCreado;
	}
}
