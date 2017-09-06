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
public class Perfil {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nombre;
	private String descripcion;
	private Date fechaAlta;
	private Date fechaBaja;
  
	public Long getId(){
		return this.id;
	}
	public void setId(Long id){
		this.id = id;
	}
}
