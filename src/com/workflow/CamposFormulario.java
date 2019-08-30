package com.workflow;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CamposFormulario implements KvmSerializable {
	/* Variables de clase */
	public String campoNombre;
	public String campoValor;
	
	/* Constructores */
	public CamposFormulario() {
		campoNombre = "";
		campoValor = "";
	}
	
	public CamposFormulario(String campoNombre, String campoValor) {
		this.campoNombre = campoNombre;
		this.campoValor = campoValor;
	}
	
	@Override
	public Object getProperty(int indice) {
		switch (indice) {
		case 0:
			return campoNombre;
			
		case 1:
			return campoValor;
		}
		
		return null;
	}
	
	@Override
	public int getPropertyCount() {
		return 2;
	}
	
	@Override
	public void getPropertyInfo(int indice, Hashtable h, PropertyInfo p) {
		switch (indice) {
		case 0:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "campoNombre";
			break;
			
		case 1:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "campoValor";
			break;
		
		default:
			break;
		}
	}
	
	@Override
	public void setProperty(int indice, Object valor) {
		switch (indice) {
		case 0:
			campoNombre = valor.toString();
			break;
			
		case 1:
			campoValor = valor.toString();
			break;
		
		default:
			break;
		}
	}

}
