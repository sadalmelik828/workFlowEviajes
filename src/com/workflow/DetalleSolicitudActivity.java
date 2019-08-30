package com.workflow;

import java.util.Vector;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleSolicitudActivity extends Activity {
	/* Variables */
	private TextView campoNombreFlujo;
	private TextView campoNombreEstado;
	private TextView campoNombreCreador;
	private TextView campoFechaSolicitud;
	private TextView campoCiudad;
	private LinearLayout contenedorFormulario;
	private ProgressDialog ventanaProgreso;
	private Vector<CamposFormulario> campos;
	private String[] resultadoRechazoAprobacion;
	private int idSolicitud;
	private int idPasoActual;
	private String eleccion;
	private String mensajeRespuesta;
	private boolean recargaLista = false;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.detalle_solicitud);
		final Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		/* Captura de campos de la vista */
		campoNombreFlujo = (TextView) findViewById(R.id.dsValorNombreFlujo);
		campoNombreEstado = (TextView) findViewById(R.id.dsValorNombreEstado);
		campoNombreCreador = (TextView) findViewById(R.id.dsValorNombreCreador);
		campoFechaSolicitud = (TextView) findViewById(R.id.dsValorFechaSolicitud);
		campoCiudad = (TextView) findViewById(R.id.dsValorNombreCiudad);
		/*
		 * Captura de datos enviados a traves del Intent y definición en el
		 * texto respectivo
		 */
		campoNombreFlujo.setText(extras.getString("nombreFlujo"));
		campoNombreEstado.setText(extras.getString("estado"));
		campoNombreCreador.setText(extras.getString("usuarioCreador"));
		campoFechaSolicitud.setText(extras.getString("fechaSolicitud"));
		campoCiudad.setText(extras.getString("ciudad"));
		/*
		 * Captura del identificador de la solicitud en los extras enviados por
		 * el Intent
		 */
		idSolicitud = extras.getInt("idSolicitud");
		/* Captura del contenedor donde se mostrarán los detalles */
		contenedorFormulario = (LinearLayout) findViewById(R.id.contenedorDetalleSolicitud);
		/*
		 * Captura del identificador del paso actual en los extras enviado por
		 * el Intent
		 */
		idPasoActual = extras.getInt("idPasoActual");
		/* Llamada al método que consulta y muestra los detalles de la solicitud */
		cargaDatos(1);
	}

	/* Método para crear campos de texto en la plantilla */
	private TextView creaTexto(String texto) {
		/* Creación de elemento TextView */
		TextView campo = new TextView(this);
		campo.setText(texto);
		return campo;
	}

	/* Método para crear botones en la plantilla */
	private Button creaBoton(String texto) {
		/* Creación del elemento Button */
		Button boton = new Button(this);
		boton.setText(texto);
		return boton;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mensajeRespuesta = "Se ha cancelado el proceso con la solicitud Nro. "
					+ idSolicitud;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * Método que consume el servicio web con un hilo de ejecución mientras
	 * muestra un mensaje informativo y llama el método de proceso respectivo
	 */
	public void cargaDatos(final int operacion) {
		String mensaje = "";
		if (operacion == 1) {
			mensaje = "Obteniendo Datos...";
		} else if (operacion == 2) {
			mensaje = "Enviando Datos...";
		}
		/* muestra ventana de progreso mientras consume el servicio web */
		ventanaProgreso = ProgressDialog.show(this, "", mensaje, true);
		/* Creación de la variable que contiene un manejador */
		final Handler manejador = new Handler() {
			@SuppressWarnings("unchecked")
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					if (operacion == 1) {
						campos = (Vector<CamposFormulario>) msg.obj;
						datosFormulario(campos);

					} else if (operacion == 2) {
						resultadoRechazoAprobacion = (String[]) msg.obj;
						rechazaApruebaSolicitud(resultadoRechazoAprobacion);
					}
				}
				ventanaProgreso.dismiss();
			}
		};
		/* Creación de hilo de ejecución */
		new Thread(new Runnable() {

			@Override
			public void run() {
				/* Inicialización de clase de servicio web */
				ServicioWebSoap sw = new ServicioWebSoap();
				/*
				 * Inicialización del manejador de progreso para almacenar los
				 * datos a obtener
				 */
				Message msg = manejador.obtainMessage();
				/* Consumo de servicio web según la operación */
				if (operacion == 1) {
					msg.obj = sw.detalleSolicitud(idSolicitud);
				} else if (operacion == 2) {
					msg.obj = sw.rechazoAprobacionSolicitud(idSolicitud,
							eleccion, idPasoActual);
				}
				/* Envío de los datos obtenidos */
				manejador.sendMessage(msg);
			}
		}).start();
	}

	/*
	 * Método que muestra los datos devueltos por el servicio web y crea los
	 * botones de aprobación o rechazo de solicitud
	 */
	public void datosFormulario(Vector<CamposFormulario> campos) {
		if (campos.size() > 0) {
			/*
			 * bucle para crear los textos y mostrar la información obtenida por
			 * el servicio web
			 */
			for (int d = 0; d < campos.size(); d++) {
				/* creación de campos */
				CamposFormulario cf = campos.elementAt(d);
				TextView nombre = this.creaTexto(cf.campoNombre + ":");
				nombre.setTypeface(Typeface.DEFAULT_BOLD);
				TextView valor = this.creaTexto(cf.campoValor);
				valor.setPadding(0, 5, 0, 5);
				/* adición de campos a la plantilla */
				contenedorFormulario.addView(nombre);
				contenedorFormulario.addView(valor);
			}
			/* creación de botones */
			Button botonAprobar = this.creaBoton("Aprobar");
			botonAprobar.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			/*
			 * Sobreescritura del evento click del boton de aprobar para consumo
			 * de servicio web
			 */
			botonAprobar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					eleccion = "APROBADO";
					cargaDatos(2);
				}
			});
			Button botonRechazar = this.creaBoton("Rechazar");
			botonRechazar.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			/*
			 * sobreescritura del evento click del boton de rechazar para
			 * consumo de servicio web
			 */
			botonRechazar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					eleccion = "RECHAZADO";
					cargaDatos(2);
				}
			});
			/* adición de botones a la plantilla */
			contenedorFormulario.addView(botonAprobar);
			contenedorFormulario.addView(botonRechazar);
		} else {
			Toast.makeText(this,
					"Ha ocurrido un problema con la petición de los detalles.",
					Toast.LENGTH_LONG);
		}
	}

	/*
	 * Método que devuelve al listado de solicitudes o muestra error de acuerdo
	 * a la respuesta del servicio web
	 */
	public void rechazaApruebaSolicitud(String[] resultado) {
		/* verificación del resultado del consumo */
		if (resultado[0].equals("OK")) {
			mensajeRespuesta = "La solicitud Nro." + idSolicitud
					+ " ha sido procesada.";
			recargaLista = true;
			finish();
		} else {
			Toast.makeText(getApplicationContext(), resultado[1],
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void finish() {
		Intent datosRespuesta = new Intent();
		datosRespuesta.putExtra("resultadoActividad", mensajeRespuesta);
		datosRespuesta.putExtra("recargaLista", recargaLista);
		setResult(RESULT_OK, datosRespuesta);
		super.finish();
	}
}
