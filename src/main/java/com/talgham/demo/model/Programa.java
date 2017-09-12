package com.talgham.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Programa {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nombre;
	private Boolean activo;
	private Date fechaProximo;
	private Date fechaUltimo;
	
	private Date fechaAlta;
	private Date fechaModificado;
	private Date fechaBaja;

	@ManyToOne @JoinColumn(name="trabajo_id")
	private Trabajo trabajo;
	@ManyToOne @JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	
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
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	public Date getFechaProximo() {
		return fechaProximo;
	}
	public void setFechaProximo(Date fechaProximo) {
		this.fechaProximo = fechaProximo;
	}
	public Date getFechaUltimo() {
		return fechaUltimo;
	}
	public void setFechaUltimo(Date fechaUltimo) {
		this.fechaUltimo = fechaUltimo;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaModificado() {
		return fechaModificado;
	}
	public void setFechaModificado(Date fechaModificado) {
		this.fechaModificado = fechaModificado;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public Trabajo getTrabajo() {
		return trabajo;
	}
	public void setTrabajo(Trabajo trabajo) {
		this.trabajo = trabajo;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
