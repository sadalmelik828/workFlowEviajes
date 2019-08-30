package com.workflow;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Solicitudes implements KvmSerializable {
	/* Variables */
	public int idSolicitud;
	public String ciudad;
	public String nombreFlujo;
	public int idPasoActual;
	public String estado;
	public String creador;
	public String observaciones;
	public String fechaSolicitud;
	
	/* Constructores */
	public Solicitudes() {
		idSolicitud = 0;
		ciudad = "";
		nombreFlujo = "";
		idPasoActual = 0;
		estado = "";
		creador = "";
		observaciones = "";
		fechaSolicitud = "";
	}
	
	public Solicitudes(int idSolicitud, String ciudad, String nombreFlujo, int idPasoActual, String estado, String creador, String observaciones, String fechaSolicitud) {
		this.idSolicitud = idSolicitud;
		this.ciudad = ciudad;
		this.nombreFlujo = nombreFlujo;
		this.idPasoActual = idPasoActual;
		this.estado = estado;
		this.creador = creador;
		this.observaciones = observaciones;
		this.fechaSolicitud = fechaSolicitud;
	}
	
	@Override
	public Object getProperty(int indice) {
		switch (indice) {
		case 0:
			return idSolicitud;
			
		case 1:
			return ciudad;
			
		case 2:
			return nombreFlujo;
			
		case 3:
			return idPasoActual;
			
		case 4:
			return estado;
		
		case 5:
			return creador;
			
		case 6:
			return observaciones;
			
		case 7:
			return fechaSolicitud;
		}
		
		return null;
	}
	
	@Override
	public int getPropertyCount() {
		return 8;
	}
	
	@Override
	public void getPropertyInfo(int indice, Hashtable h, PropertyInfo p) {
		switch (indice) {
		case 0:
			p.type = PropertyInfo.INTEGER_CLASS;
			p.name = "idSolicitud";
			break;
			
		case 1:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "ciudad";
			break;
		
		case 2:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "nombreFlujo";
			break;
			
		case 3:
			p.type = PropertyInfo.INTEGER_CLASS;
			p.name = "idPasoActual";
			break;
			
		case 4:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "estado";
			break;
			
		case 5:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "creador";
			break;
			
		case 6:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "observaciones";
			break;
			
		case 7:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "fechaSolicitud";
			break;

		default:
			break;
		}
	}
	
	@Override
	public void setProperty(int indice, Object valor) {
		switch (indice) {
		case 0:
			idSolicitud = Integer.parseInt(valor.toString());
			break;
			
		case 1:
			ciudad = valor.toString();
			break;
			
		case 2:
			nombreFlujo = valor.toString();
			break;
			
		case 3:
			idPasoActual = Integer.parseInt(valor.toString());
			break;
			
		case 4:
			estado = valor.toString();
			break;
			
		case 5:
			creador = valor.toString();
			break;
			
		case 6:
			observaciones = valor.toString();
			break;
			
		case 7:
			fechaSolicitud = valor.toString();
			break;

		default:
			break;
		}
	}

}
