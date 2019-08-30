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

public class ActualizaClaveActivity extends Activity {
	/* Variables */
	private EditText cajaClaveActual;
	private EditText cajaClaveNueva1;
	private EditText cajaClaveNueva2;
	private Button botonActualiza;
	private String mensajeRespuesta;
	private int idUsuario;
	private String claveActual;
	private String claveNueva1;
	private String claveNueva2;
	private String[] resultado;
	private ProgressDialog ventanaProgreso;
	private Handler manejador = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				resultado = (String[]) msg.obj;
				procesaResultado(resultado);
			}
			ventanaProgreso.dismiss();
		}
	};
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.actualiza_clave);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		idUsuario = Integer.parseInt(extras.getString("idUsuario"));
		/* captura de los elementos de la vista */
		cajaClaveActual = (EditText) findViewById(R.id.acCampoClaveActual);
		cajaClaveNueva1 = (EditText) findViewById(R.id.acCampoClaveNueva1);
		cajaClaveNueva2 = (EditText) findViewById(R.id.acCampoClaveNueva2);
		botonActualiza = (Button) findViewById(R.id.btActualizaClave);
		/* Sobreescritura del evento click en el boton de actualizar la contrase�a */
		botonActualiza.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/* captura de valores ingresados en los campos de texto */
				claveActual = cajaClaveActual.getText().toString();
				claveNueva1 = cajaClaveNueva1.getText().toString();
				claveNueva2 = cajaClaveNueva2.getText().toString();
				/* Inicializaci�n de clase de control de login para encriptar las claves */
				ControlLogin cl = new ControlLogin(getApplicationContext());
				/* verificacion de valores */
				if (!claveActual.isEmpty()) {
					claveActual = cl.md5(claveActual);
					/* verificaci�n de contrase�a nueva */
					if (!claveNueva1.isEmpty()) {
						/* Verifica la igualdad de la confirmaci�n de la contrase�a nueva */
						if (claveNueva1.equals(claveNueva2)) {
							/* encriptacion de clave nueva */
							claveNueva1 = cl.md5(claveNueva1);
							/* Llamada a m�todo para consumir servicio web que actualiza la contrase�a del usuario */
							enviaDatos();
						} else {
							Toast.makeText(getApplicationContext(), "La contrase�a nueva no coincide", Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "Debe introducir una contrase�a nueva", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "Debe introducir la contrase�a actual", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	/* M�todo que consume el servicio web en un hilo de ejecuci�n mientras muestra un mensaje informativo */
	private void enviaDatos() {
		/* muestra ventana de progreso */
		ventanaProgreso = ProgressDialog.show(this, "", "Enviando Datos...", true);
		/* creaci�n de hilo de ejecuci�n */
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				/* Inicializaci�n de clase de servicio web */
				ServicioWebSoap sw = new ServicioWebSoap();
				/* Inicializador de mensaje con el manejador */
				Message msg = manejador.obtainMessage();
				/* consumo de servicio web para actualizar la clave del usuario */
				msg.obj = sw.actualizaClave(idUsuario, claveActual, claveNueva1);
				/* envio de mensaje al manejador */
				manejador.sendMessage(msg);
			}
		}).start();
	}
	
	/* M�todo que procesa el resultado del consumo de servicio web */
	private void procesaResultado(String[] resultado) {
		if (resultado.length > 0) {
			/* verificaci�n del estado del resultado */
			if (resultado[0].equals("OK")) {
				mensajeRespuesta = "La contrase�a ha sido actualizada.";
				finish();
			} else {
				Toast.makeText(this, resultado[1], Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Ha ocurrido un problema con el servicio web", Toast.LENGTH_LONG).show();
		}
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
			this.mensajeRespuesta = "Se ha cancelado la actualizaci�n de contrase�a.";
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
