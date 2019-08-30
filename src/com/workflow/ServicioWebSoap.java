package com.workflow;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.Vector;

public class ServicioWebSoap {
	/* Variables */
	final static String NAMESPACE = "http://177.71.178.185/webservice/";
//	final static String NAMESPACE = "http://workfloweviajes/webservice/";
//	final static String URL = "http://10.0.2.2/webservice/ManejadorWebService.php";
	final static String URL = "http://177.71.178.185/webservice/ManejadorWebService.php";
	
	/* Constructores */
	public ServicioWebSoap() {
		
	}
	
	/* Método de consumo para el inicio de sesión */
	@SuppressWarnings("unchecked")
	public String[] iniciaSesion(String usuario, String password) {
		/* Variables */
		String[] retorno = new String[3];
		Vector<SoapObject> resultado = new Vector<SoapObject>(3);
		/* Definición del nombre y url de la operación */
		String metodo = "iniciaSesion";
		String soap_action = NAMESPACE+metodo;
		/* Inicialización de la petición del servicio web */
		SoapObject peticion = new SoapObject(NAMESPACE, metodo);
		/* Adición de parámetros con sus valores para la petición */
		peticion.addProperty("usuario", usuario);
		peticion.addProperty("password", password);
		/* Definición del tipo de servicio web */
		SoapSerializationEnvelope en = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/* Adición de la petición al tipo de servicio web */
		en.setOutputSoapObject(peticion);
		/* Inicialización de transporte para el servicio web */
		HttpTransportSE transporte = new HttpTransportSE(URL);
		try {
			/* Inicio del transporte */
			transporte.call(soap_action, en);
			/* Recepción de respuesta */
			resultado = (Vector<SoapObject>) en.getResponse();
			if (resultado != null) {
				for (int i = 0; i < resultado.size(); i++) {
					retorno[i] = String.valueOf(resultado.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	/* Método que consume el servicio web para obtener los datos actuales del usuario */
	@SuppressWarnings("unchecked")
	public DatosUsuario datosUsuario(int idUsuario) {
		/* Variables */
		DatosUsuario resultado = new DatosUsuario();
		Vector<SoapObject> datos = new Vector<SoapObject>(7);
		/* Definición del nombre y url de la operación */
		String metodo = "datosUsuario";
		String soap_action = NAMESPACE+metodo;
		/* Inicialización de la petición del servicio web */
		SoapObject peticion = new SoapObject(NAMESPACE, metodo);
		/* Adición de parámetros con sus valores para la petición */
		peticion.addProperty("idUsuario", idUsuario);
		/* Definición del tipo de servicio web */
		SoapSerializationEnvelope en = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/* Adición de la petición al tipo de servicio web */
		en.setOutputSoapObject(peticion);
		/* Inicialización de transporte para el servicio web */
		HttpTransportSE transporte = new HttpTransportSE(URL);
		try {
			/* Inicio del transporte */
			transporte.call(soap_action, en);
			/* Recepción de respuesta */
			datos = (Vector<SoapObject>) en.getResponse();
			if (datos != null) {
				if (String.valueOf(datos.get(6)).equalsIgnoreCase("OK")) {
					resultado.Nombres = String.valueOf(datos.get(0));
					resultado.Apellidos = String.valueOf(datos.get(1));
					resultado.Email = String.valueOf(datos.get(2));
					resultado.EmailSecundario = String.valueOf(datos.get(3));
					resultado.TelefonoCelular = String.valueOf(datos.get(4));
					resultado.TelefonoFijo = String.valueOf(datos.get(5));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}
	
	/* Método de consumo de servicio web para actualizar datos del usuario en la base de datos del servidor */
	@SuppressWarnings("unchecked")
	public String[] actualizaDatos(DatosUsuario datos) {
		/* Variables */
		String[] retorno = new String[3];
		Vector<SoapObject> resultado = new Vector<SoapObject>(3);
		/* Definición del nombre y url de la operación */
		String metodo = "actualizaDatos";
		String soap_action = NAMESPACE+metodo;
		/* Inicialización de la petición del servicio web */
		SoapObject peticion = new SoapObject(NAMESPACE, metodo);
		/* Adición de parámetros con sus valores para la petición */
		peticion.addProperty("Id", datos.Id);
		peticion.addProperty("Nombres", datos.Nombres);
		peticion.addProperty("Apellidos", datos.Apellidos);
		peticion.addProperty("Email", datos.Email);
		peticion.addProperty("EmailSecundario", datos.EmailSecundario);
		peticion.addProperty("TelefonoCelular", datos.TelefonoCelular);
		peticion.addProperty("TelefonoFijo", datos.TelefonoFijo);
		/* Definición del tipo de servicio web */
		SoapSerializationEnvelope en = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/* Adición de la petición al tipo de servicio web */
		en.setOutputSoapObject(peticion);
		/* Inicialización de transporte para el servicio web */
		HttpTransportSE transporte = new HttpTransportSE(URL);
		try {
			/* Inicio del transporte */
			transporte.call(soap_action, en);
			/* Recepción de respuesta */
			resultado = (Vector<SoapObject>) en.getResponse();
			if (resultado != null) {
				for (int i = 0; i < resultado.size(); i++) {
					retorno[i] = String.valueOf(resultado.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	/* Método de consumo para obtener las solicitudes por aprobar o rechazar */
	public Vector<Solicitudes> listaSolicitudes(int idUsuario) {
		/* Variables */
		Vector<Solicitudes> listaSolicitud = new Vector<Solicitudes>();
		/* Definición del nombre y url de la operación */
		String metodo = "listaSolicitudes";
		String soap_action = NAMESPACE+metodo;
		/* Inicialización de la petición del servicio web */
		SoapObject peticion = new SoapObject(NAMESPACE, metodo);
		/* Adición de parámetros con sus valores para la petición */
		peticion.addProperty("idUsuario", idUsuario);
		/* Definición del tipo de servicio web */
		SoapSerializationEnvelope en = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/* Adición de la petición al tipo de servicio web */
		en.setOutputSoapObject(peticion);
		/* Inicialización de transporte para el servicio web */
		HttpTransportSE transporte = new HttpTransportSE(URL);
		try {
			/* Inicio del transporte */
			transporte.call(soap_action, en);
			/* Recepción de respuesta */
			KvmSerializable respuesta = (KvmSerializable) en.bodyIn;
			/* Bucle para capturar las solicitudes y almacenarlas en el vector de solicitudes */
			for (int i = 0; i < respuesta.getPropertyCount(); i++) {
		           SoapObject ic = (SoapObject)respuesta.getProperty(i);
		           Solicitudes solicitud = new Solicitudes();
		           solicitud.idSolicitud = Integer.parseInt(ic.getProperty(0).toString());
		           solicitud.ciudad = ic.getProperty(1).toString();
		           solicitud.nombreFlujo = ic.getProperty(2).toString();
		           solicitud.idPasoActual = Integer.parseInt(ic.getProperty(3).toString());
		           solicitud.estado = ic.getProperty(4).toString();
		           solicitud.creador = ic.getProperty(5).toString();
		           solicitud.observaciones = ic.getProperty(6).toString();
		           solicitud.fechaSolicitud = ic.getProperty(7).toString();
		           listaSolicitud.addElement(solicitud);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* Retorno */
		return listaSolicitud;
	}
	
	/* Método de consumo de servicio web para obtener los detalles de una solicitud */
	public Vector<CamposFormulario> detalleSolicitud(int idSolicitud) {
		/* Variables */
		Vector<CamposFormulario> detalleSolicitud = new Vector<CamposFormulario>();
		/* Definición del nombre y url de la operación */
		String metodo = "detalleSolicitud";
		String soap_action = NAMESPACE+metodo;
		/* Inicialización de la petición del servicio web */
		SoapObject peticion = new SoapObject(NAMESPACE, metodo);
		/* Adición de parámetros con sus valores para la petición */
		peticion.addProperty("idSolicitud", idSolicitud);
		/* Definición del tipo de servicio web */
		SoapSerializationEnvelope en = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/* Adición de la petición al tipo de servicio web */
		en.setOutputSoapObject(peticion);
		/* Inicialización de transporte para el servicio web */
		HttpTransportSE transporte = new HttpTransportSE(URL);
		try {
			/* Inicio del transporte */
			transporte.call(soap_action, en);
			/* Recepción de respuesta */
			KvmSerializable respuesta = (KvmSerializable) en.bodyIn;
			/* Bucle para capturar los detalles de la solicitud */
			for (int d = 0; d < respuesta.getPropertyCount(); d++) {
				/* Captura de objeto */
				SoapObject c = (SoapObject)respuesta.getProperty(d);
				/* Inicialización de objeto tipo CampoFormulario */
				CamposFormulario campo = new CamposFormulario();
				/* definición de valores */
				campo.campoNombre = c.getProperty(0).toString();
				campo.campoValor = c.getProperty(1).toString();
				/* adición del objeto CampoFormulario al vector */
				detalleSolicitud.addElement(campo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* Retorno */
		return detalleSolicitud;
	}
	
	/* Método de consumo de servicio web para rechazar o aprobar una solicitud */
	@SuppressWarnings("unchecked")
	public String[] rechazoAprobacionSolicitud(int idSolicitud, String eleccion, int idPasoActual) {
		/* Variables */
		String[] resultado = new String[2];
		Vector<SoapObject> datos = new Vector<SoapObject>(2);
		/* Definición del nombre y url de la operación */
		String metodo = "rechazoAprobacionSolicitud";
		String soap_action = NAMESPACE+metodo;
		/* Inicialización de la petición del servicio web */
		SoapObject peticion = new SoapObject(NAMESPACE, metodo);
		/* Adición de parámetros con sus valores para la petición */
		peticion.addProperty("idSolicitud", idSolicitud);
		peticion.addProperty("eleccionPaso", eleccion);
		peticion.addProperty("idPasoActual", idPasoActual);
		/* Definición del tipo de servicio web */
		SoapSerializationEnvelope en = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/* Adición de la petición al tipo de servicio web */
		en.setOutputSoapObject(peticion);
		/* Inicialización de transporte para el servicio web */
		HttpTransportSE transporte = new HttpTransportSE(URL);
		try {
			/* Inicio del transporte */
			transporte.call(soap_action, en);
			/* Recepción de respuesta */
			datos = (Vector<SoapObject>) en.getResponse();
			if (datos != null) {
				for (int i = 0; i < datos.size(); i++) {
					resultado[i] = String.valueOf(datos.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}
	
	/* Método de consumo de servicio web para actualizar la clave del usuario en sesión */
	@SuppressWarnings("unchecked")
	public String[] actualizaClave(int idUsuario, String claveActual, String claveNueva) {
		/* Variables */
		String[] resultado = new String[2];
		Vector<SoapObject> datos = new Vector<SoapObject>(2);
		/* Definición del nombre y url de la operación */
		String metodo = "actualizaClave";
		String soap_action = NAMESPACE+metodo;
		/* Inicialización de la petición del servicio web */
		SoapObject peticion = new SoapObject(NAMESPACE, metodo);
		/* Adición de parámetros con sus valores para la petición */
		peticion.addProperty("idUsuario", idUsuario);
		peticion.addProperty("claveActual", claveActual);
		peticion.addProperty("claveNueva", claveNueva);
		/* Definición del tipo de servicio web */
		SoapSerializationEnvelope en = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/* Adición de la petición al tipo de servicio web */
		en.setOutputSoapObject(peticion);
		/* Inicialización de transporte para el servicio web */
		HttpTransportSE transporte = new HttpTransportSE(URL);
		try {
			/* Inicio del transporte */
			transporte.call(soap_action, en);
			/* Recepción de respuesta */
			datos = (Vector<SoapObject>) en.getResponse();
			if (datos != null) {
				for (int i = 0; i < datos.size(); i++) {
					resultado[i] = String.valueOf(datos.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}
}
