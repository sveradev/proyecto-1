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
	private String titulo;
	private String descripcion;
	private Boolean programada;
	
	private Date fechaCreacion;
	private Date fechaSolicitado;
	private Date fechaModificado;
	private Date fechaFinalizado;
	@ManyToOne @JoinColumn(name="estado_id")
	private Estado estado;
	@ManyToOne @JoinColumn(name="usuario_id")
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
		return estado.getOrden() == Constantes.ESTADO_SOLICITADO && this.fechaSolicitado.before(new Date()); //ver
	}
}