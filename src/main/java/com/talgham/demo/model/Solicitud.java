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
	private String titulo;
	@ManyToOne @JoinColumn(name="trabajo_id")
	private Trabajo trabajo;
	private String descripcion;
	
	private Date fechaCreacion;
	private Date fechaSolicitado;
	private Date fechaModificado;
	private Date fechaFinalizado;
	@ManyToOne @JoinColumn(name="estado_id")
	private Estado estado;
	@ManyToOne @JoinColumn(name="usuario_id")
	private Cliente cliesnte;
}
