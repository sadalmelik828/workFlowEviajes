package com.workflow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class actualizarDatosActivity extends Activity {
	EditText campoNombres;
	EditText campoApellidos;
	EditText campoEmail;
	EditText campoEmailSecundario;
	EditText campoTelefonoFijo;
	EditText campoTelefonoMovil;
	Button botonActualizar;
	String mensajeRespuesta;
	ProgressDialog ventanaProgreso;
	int idUsuario;
	DatosUsuario datosUsuario, datos;
	String[] actualizacionDatos;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.actualizar_datos);
		/* Captura de los datos enviados desde el activity del menu principal */
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		/*
		 * Captura del identificador del usuario, que esta incluido en los
		 * extras
		 */
		idUsuario = Integer.parseInt(extras.getString("idUsuario"));
		/* captura de los campos del formulario */
		campoNombres = (EditText) findViewById(R.id.adCampoNombres);
		campoApellidos = (EditText) findViewById(R.id.adCampoApellidos);
		campoEmail = (EditText) findViewById(R.id.adCampoEmail);
		campoEmailSecundario = (EditText) findViewById(R.id.adCampoEmailSecundario);
		campoTelefonoFijo = (EditText) findViewById(R.id.adCampoTelefonoFijo);
		campoTelefonoMovil = (EditText) findViewById(R.id.adCampoTelefonoMovil);
		/* Captura del boton que actualiza los datos del usuario */
		botonActualizar = (Button) findViewById(R.id.btActualizaDatos);
		/*
		 * Llamada a m�todo que consume el servicio web y coloca los datos en
		 * los campos de texto
		 */
		transmiteDatos(1);
	}

	@Override
	public void finish() {
		Intent datosRespuesta = new Intent();
		datosRespuesta.putExtra("resultadoActividad", mensajeRespuesta);
		setResult(RESULT_OK, datosRespuesta);
		super.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.mensajeRespuesta = "Se ha cancelado la actualizaci�n de datos.";
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * M�todo que consume el servicio web segun la operaci�n mientras que
	 * muestra un mensaje informativo
	 */
	public void transmiteDatos(final int operacion) {
		String mensaje = "";
		if (operacion == 1) {
			mensaje = "Obteniendo Datos...";
		} else if (operacion == 2) {
			mensaje = "Enviando Datos...";
		}
		/* muestra ventana de progreso mientras consume el servicio web */
		ventanaProgreso = ProgressDialog.show(this, "", mensaje, true);
		/* Creaci�n de la variable que contiene un manejador */
		final Handler manejador = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					if (operacion == 1) {
						datosUsuario = (DatosUsuario) msg.obj;
						consultaDatos(datosUsuario);

					} else if (operacion == 2) {
						actualizacionDatos = (String[]) msg.obj;
						actualizaDatos(actualizacionDatos);
					}
				}
				ventanaProgreso.dismiss();
			}
		};
		/* Creaci�n de hilo de ejecuci�n */
		new Thread(new Runnable() {

			@Override
			public void run() {
				/* Inicializaci�n de clase de servicio web */
				ServicioWebSoap sw = new ServicioWebSoap();
				/*
				 * Inicializaci�n del manejador de progreso para almacenar los
				 * datos a obtener
				 */
				Message msg = manejador.obtainMessage();
				/* Consumo de servicio web seg�n la operaci�n */
				if (operacion == 1) {
					msg.obj = sw.datosUsuario(idUsuario);
				} else if (operacion == 2) {
					msg.obj = sw.actualizaDatos(datos);
				}
				/* Env�o de los datos obtenidos */
				manejador.sendMessage(msg);
			}
		}).start();
	}

	/* M�todo que consulta los datos del usuario y los muestra */
	public void consultaDatos(DatosUsuario infoUsuario) {
		/* Verificaci�n de datos en la respuesta del consumo del servicio web */
		if (!infoUsuario.Nombres.isEmpty()) {
			/*
			 * definici�n de los valores actuales del usuario en los campos del
			 * formulario
			 */
			campoNombres.setText(infoUsuario.Nombres);
			campoApellidos.setText(infoUsuario.Apellidos);
			campoEmail.setText(infoUsuario.Email);
			if (infoUsuario.EmailSecundario != "null") {
				campoEmailSecundario.setText(infoUsuario.EmailSecundario);
			}
			if (infoUsuario.TelefonoFijo != "null") {
				campoTelefonoFijo.setText(infoUsuario.TelefonoFijo);
			}
			if (infoUsuario.TelefonoCelular != "null") {
				campoTelefonoMovil.setText(infoUsuario.TelefonoCelular);
			}
		} else {
			Toast.makeText(getApplicationContext(), "Ha ocurrido un error con el servicio web.",
					Toast.LENGTH_LONG).show();
		}
		/* Sobreescritura de evento click en el boton de actualizar */
		botonActualizar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/* Inicializaci�n de clase de datosUsuario */
				datos = new DatosUsuario();
				/*
				 * Definici�n de datos con base en la informaci�n incluida en el
				 * formulario
				 */
				datos.Id = idUsuario;
				datos.Nombres = campoNombres.getText().toString();
				datos.Apellidos = campoApellidos.getText().toString();
				datos.Email = campoEmail.getText().toString();
				datos.EmailSecundario = campoEmailSecundario.getText().toString();
				datos.TelefonoFijo = campoTelefonoFijo.getText().toString();
				datos.TelefonoCelular = campoTelefonoMovil.getText().toString();
				transmiteDatos(2);
			}
		});
	}

	/* M�todo que procesa la respuesta al enviar la actualizaci�n al servidor */
	public void actualizaDatos(String[] respuesta) {
		/* Verificaci�n de la respuesta del consumo del servicio web */
		if (respuesta[0].equals("OK")) {
			mensajeRespuesta = "Los datos de usuario se han actualizado.";
		} else {
			mensajeRespuesta = "No se han podido actualizar los datos.";
		}
		finish();
	}
}