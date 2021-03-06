package com.talgham.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.talgham.demo.common.Constantes;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nombre;
	private String alias;
	@Column(unique = true)
	private String email;
	private Integer nroDocumento;
	private String tipoDocumento;
	private Date fechaAlta;
	private Date fechaBaja;
	private String password;
	@ManyToOne @JoinColumn(name="rol_id")
	private Rol rol;
	@ManyToOne @JoinColumn(name="perfil_id")
	private Perfil perfil;
	
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public Integer getNroDocumento() {
		return nroDocumento;
	}
	public void setNroDocumento(Integer nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Boolean isActive() {
		return fechaBaja == null;
	}
	public Boolean isDev() {
		return perfil.getOrden() == Constantes.PERFIL_DESARROLLADOR; 
	}
	public Boolean isAdmin() {
		return perfil.getOrden() <= Constantes.PERFIL_ADMINISTRADOR || rol.getOrden() == Constantes.ROL_ADMINISTRADOR ; 
	}
	public Boolean isContador() {
		return perfil.getOrden() == Constantes.PERFIL_CONTADOR;
	}
	public Boolean isCliente() {
		return perfil.getOrden() == Constantes.PERFIL_CLIENTE;
	}
}
