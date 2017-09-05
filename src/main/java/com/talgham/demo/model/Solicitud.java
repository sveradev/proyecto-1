package com.talgham.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.talgham.demo.common.Constantes;
@Entity
public class Solicitud {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nombre;
	@ManyToOne @JoinColumn(name="trabajo_id")
	private Trabajo trabajo;
	private String email;
	private String descripcion;
	private Date fechaSolicitado;
	private Date fechaModificado;
	private Date fechaVencimiento;
	private Date fechaFinalizado;
	@ManyToOne @JoinColumn(name="estado_id")
	private Estado estado;
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
	public Trabajo getTrabajo() {
		return trabajo;
	}
	public void setTrabajo(Trabajo trabajo) {
		this.trabajo = trabajo;
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
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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
	
	public Boolean isSuccess() {
		return estado.getId() == Constantes.ESTADO_SOLICITADO && new Date().before(addDays(fechaSolicitado, 7));
	}
	public Boolean isPending() {
		return estado.getId() == Constantes.ESTADO_SOLICITADO && new Date().after(addDays(fechaSolicitado, 7));
	}
	public Boolean isOverdue() {
		return estado.getId() == Constantes.ESTADO_SOLICITADO && new Date().after(addDays(fechaSolicitado, 15));
	}
	public Boolean isActive() {
		return estado.getId() != Constantes.ESTADO_SOLICITADO && estado.getId() != Constantes.ESTADO_FINALIZADO;
	}
}
