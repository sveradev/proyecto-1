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
public class Programa {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String nombre;
  private String descripcion;
  private Integer periodicidad;
  private Date fechaSolicitado;
  private Date fechaUltimoCreado;
  
  private Date fechaAlta
	private Date fechaModificado;
	private Date fechaBaja;
  
	@ManyToOne @JoinColumn(name="cliente_id")
	private Cliente cliente;

}
