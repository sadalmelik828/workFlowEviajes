package com.workflow;

import java.util.Vector;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class ListaSolicitudesActivity extends Activity {
	/* Variables */
	ListView lista;
	int usuario;
	Vector<Solicitudes> listaSolicitud;
	AdaptadorSolicitudes adaptador;
	private static final int CODIGO_PETICION = 1119;
	private ProgressDialog dialogoProgreso;
	private final Handler manejadorProgreso = new Handler() {
		@SuppressWarnings({ "unchecked" })
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				listaSolicitud = (Vector<Solicitudes>) msg.obj;
				muestraLista(listaSolicitud);
			}
			dialogoProgreso.dismiss();
		}
	};

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.lista_solicitudes);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		final String idUsuario = extras.getString("idUsuario");
		/* Captura de elemento de lista */
		lista = (ListView) findViewById(R.id.elementoListaSolicitudes);
		if (idUsuario != null) {
			usuario = Integer.parseInt(idUsuario);
			/* Llamada a método para cargar la lista de solicitudes */
			cargaSolicitudes();
		} else {
			Toast.makeText(getApplicationContext(), "No existen los datos de proceso.", Toast.LENGTH_LONG).show();
		}
	}
	
	/* Método para cargar la lista de solicitudes */
	private void muestraLista(final Vector<Solicitudes> listaSolicitud) {
		/* Verificación de existencia de solicitudes */
		if (listaSolicitud.size() > 0) {
			adaptador = new AdaptadorSolicitudes(this, listaSolicitud);
			lista.setAdapter(adaptador);
			/* Sobreescritura del listener de la lista */
			lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int posicion, long id) {
					/* Captura del objeto solicitud */
					Solicitudes sol = listaSolicitud.elementAt(posicion);
					/* Inicialización del Intent */
					Intent ventanaDetalleSolicitud = new Intent(getApplicationContext(), DetalleSolicitudActivity.class);
					/* Adición de valores a enviar por el Intent */
					ventanaDetalleSolicitud.putExtra("idSolicitud", sol.idSolicitud);
					ventanaDetalleSolicitud.putExtra("nombreFlujo", sol.nombreFlujo);
					ventanaDetalleSolicitud.putExtra("idPasoActual", sol.idPasoActual);
					ventanaDetalleSolicitud.putExtra("estado", sol.estado);
					ventanaDetalleSolicitud.putExtra("usuarioCreador", sol.creador);
					ventanaDetalleSolicitud.putExtra("fechaSolicitud", sol.fechaSolicitud);
					ventanaDetalleSolicitud.putExtra("ciudad", sol.ciudad);
					startActivityForResult(ventanaDetalleSolicitud, CODIGO_PETICION);
				}
			});
		} else {
			Toast.makeText(getApplicationContext(), "No hay solicitudes para aprobar o rechazar.", Toast.LENGTH_LONG).show();
		}
	}
	
	/* Método que muestra un mensaje mientras que un hilo de ejecución obtiene los datos del servidor */
	private void cargaSolicitudes() {
		dialogoProgreso = ProgressDialog.show(this, "", "Obteniendo datos... ", true);
		/* Creación de nuevo hilo de ejecución */
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				/* Inicialización de clase de servicio web */
				ServicioWebSoap sw = new ServicioWebSoap();
				/* Inicialización del manejador de progreso para almacenar los datos a obtener */
				Message msg = manejadorProgreso.obtainMessage();
				/* Consumo de servicio web para obtener la lista de solicitudes */
				msg.obj = sw.listaSolicitudes(usuario);
				/* Envío de los datos obtenidos */
				manejadorProgreso.sendMessage(msg);
			}
		}).start();
	}
	
	@Override
	protected void onActivityResult(int codigoPeticion, int codigoResultado, Intent datos) {
		if (codigoResultado == RESULT_OK && codigoPeticion == CODIGO_PETICION) {
			if (datos.hasExtra("recargaLista")) {
				boolean recarga = datos.getExtras().getBoolean("recargaLista");
				/* Verifica el valor de la variables */
				if (recarga) {
					adaptador.clear();
					cargaSolicitudes();
				}
			}
			if (datos.hasExtra("resultadoActividad")) {
				/* Captura del mensaje de respuesta de la actividad que se convocó */
				String mensaje = datos.getExtras().getString("resultadoActividad");
				/* Mensaje en pantalla */
				Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
