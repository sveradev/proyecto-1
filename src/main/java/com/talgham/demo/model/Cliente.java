package com.talgham.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Cliente {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nombre;
	private String cuit;
  
	private String descripcion;
  	private Date cierreEjercicio;
	private Date fechaAlta;
	private Date fechaModificado;
	private Date fechaBaja;
  
 	@ManyToOne @JoinColumn(name="representante_id")
	private Usuario representante;
	@ManyToOne @JoinColumn(name="contador_id")
	private Usuario contador;
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
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getCierreEjercicio() {
		return cierreEjercicio;
	}
	public void setCierreEjercicio(Date cierreEjercicio) {
		this.cierreEjercicio = cierreEjercicio;
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
	public Usuario getRepresentante() {
		return representante;
	}
	public void setRepresentante(Usuario representante) {
		this.representante = representante;
	}
	public Usuario getContador() {
		return contador;
	}
	public void setContador(Usuario contador) {
		this.contador = contador;
	}
}
