package com.workflow;

import java.util.Vector;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorSolicitudes extends ArrayAdapter<Solicitudes> {
	
	Activity contexto;
	private final Vector<Solicitudes> datos;
	
	public AdaptadorSolicitudes(Activity contexto, Vector<Solicitudes> datos) {
		super(contexto, R.layout.listitem_solicitudes, datos);
		this.contexto = contexto;
		this.datos = datos;
	}
	
	@Override
	public View getView(int posicion, View vistaConvertida, ViewGroup pariente) {
		/* inicialización del item a devolver */
		View item = vistaConvertida;
		/* Inicialización del mantenedor para almacenar los datos */
		ViewHolder holder;
		
		if (item == null) {
			LayoutInflater inflater = contexto.getLayoutInflater();
			item = inflater.inflate(R.layout.listitem_solicitudes, null);
			
			holder = new ViewHolder();
			holder.titulo = (TextView) item.findViewById(R.id.lstItemTituloSolicitud);
			holder.subtitulo = (TextView) item.findViewById(R.id.lstItemSubtituloSolicitud);
			
			item.setTag(holder);
		} else {
			holder = (ViewHolder) item.getTag();
		}
		/* Definición de los valores del objeto de solicitud almacenado en el vector */
		holder.titulo.setText(String.valueOf(datos.elementAt(posicion).idSolicitud) + " - " + datos.elementAt(posicion).nombreFlujo);
		holder.subtitulo.setText(datos.elementAt(posicion).estado + " - " + datos.elementAt(posicion).creador);
		/* Retorno */
		return item;
	}
	
	static class ViewHolder {
		TextView titulo;
		TextView subtitulo;
	}

}
