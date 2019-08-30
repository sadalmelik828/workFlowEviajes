package com.workflow;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DatosUsuario implements KvmSerializable {
	/* Variables */
	public int Id;
	public String Nombres;
	public String Apellidos;
	public String Email;
	public String EmailSecundario;
	public String TelefonoCelular;
	public String TelefonoFijo;
	
	/* Constructores */
	public DatosUsuario() {
		Id = 0;
		Nombres = "";
		Apellidos = "";
		Email = "";
		EmailSecundario = "";
		TelefonoCelular = "";
		TelefonoFijo = "";
	}
	
	public DatosUsuario(int id, String nombres, String apellidos, String email, String emailSecundario, String telefonoCelular, String telefonoFijo) {
		this.Id = id;
		this.Nombres = nombres;
		this.Apellidos = apellidos;
		this.Email = email;
		this.EmailSecundario = emailSecundario;
		this.TelefonoCelular = telefonoCelular;
		this.TelefonoFijo = telefonoFijo;
	}
	
	@Override
	public Object getProperty(int indice) {
		switch (indice) {
		case 0:
			return Id;
			
		case 1:
			return Nombres;
			
		case 2:
			return Apellidos;
			
		case 3:
			return Email;
		
		case 4:
			return EmailSecundario;
			
		case 5:
			return TelefonoCelular;
			
		case 6:
			return TelefonoFijo;
		}
		
		return null;
	}
	
	@Override
	public int getPropertyCount() {
		return 7;
	}
	
	@Override
	public void getPropertyInfo(int indice, Hashtable h, PropertyInfo p) {
		switch (indice) {
		case 0:
			p.type = PropertyInfo.INTEGER_CLASS;
			p.name = "Id";
			break;
			
		case 1:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "Nombres";
			break;
		
		case 2:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "Apellidos";
			break;
			
		case 3:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "Email";
			break;
			
		case 4:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "EmailSecundario";
			break;
			
		case 5:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "TelefonoCelular";
			break;
			
		case 6:
			p.type = PropertyInfo.STRING_CLASS;
			p.name = "TelefonoFijo";
			break;

		default:
			break;
		}
	}
	
	@Override
	public void setProperty(int indice, Object valor) {
		switch (indice) {
		case 0:
			Id = Integer.parseInt(valor.toString());
			break;
			
		case 1:
			Nombres = valor.toString();
			break;
			
		case 2:
			Apellidos = valor.toString();
			break;
			
		case 3:
			Email = valor.toString();
			break;
			
		case 4:
			EmailSecundario = valor.toString();
			break;
			
		case 5:
			TelefonoCelular = valor.toString();
			break;
			
		case 6:
			TelefonoFijo = valor.toString();
			break;

		default:
			break;
		}
	}
}
