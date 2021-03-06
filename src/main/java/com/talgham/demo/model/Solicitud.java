package com.talgham.demo.model;

import java.util.Calendar;
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
	@ManyToOne @JoinColumn(name="trabajo_id")
	private Trabajo trabajo;
	private String titulo;
	private String descripcion;
	private Boolean programada;
	
	private Date fechaCreacion;
	private Date fechaSolicitado;
	private Date fechaModificado;
	private Date fechaFinalizado;
	@ManyToOne @JoinColumn(name="estado_id")
	private Estado estado;
	@ManyToOne @JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Trabajo getTrabajo() {
		return trabajo;
	}
	public void setTrabajo(Trabajo trabajo) {
		this.trabajo = trabajo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getProgramada() {
		return programada;
	}
	public void setProgramada(Boolean programada) {
		this.programada = programada;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Boolean isSuccess () {
		return estado.getOrden() == Constantes.ESTADO_SOLICITADO && new Date().compareTo(this.fechaSolicitado) >= 0;
	}
	public Boolean isPending () {
		Calendar c = Calendar.getInstance(); 
		c.setTime(this.fechaSolicitado); 
		c.add(Calendar.DATE, 3);
		Date pending = c.getTime();
		return estado.getOrden() == Constantes.ESTADO_SOLICITADO && new Date().after(pending);
	}
	public Boolean isOverdue () {
		Calendar c = Calendar.getInstance(); 
		c.setTime(this.fechaSolicitado); 
		c.add(Calendar.DATE, 7);
		Date overdue = c.getTime();
		return estado.getOrden() == Constantes.ESTADO_SOLICITADO && new Date().after(overdue);
	}
	public Boolean isActive () {
		return estado.getOrden() == Constantes.ESTADO_FINALIZADO;
	}
}