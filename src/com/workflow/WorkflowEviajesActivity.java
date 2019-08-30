package com.workflow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WorkflowEviajesActivity extends Activity {
	/* variables */
	private ControlLogin ctLogin;
	private String nombreUsuario;
	private String pass;
	private ProgressDialog ventanaProgreso;
	private String[] resultado;
	private final Handler manejador = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				resultado = (String[]) msg.obj;
				procesaRespuesta(resultado);
			}
			ventanaProgreso.dismiss();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ctLogin = (ControlLogin) findViewById(R.id.CtSesion);
		ctLogin.setLoginListener(new LoginListener() {

			@Override
			public void iniciarSesion(String usuario, String password) {
				/* definici�n del valor de usuario */
				nombreUsuario = usuario;
				/* Encriptaci�n del password a algoritmo MD5 */
				pass = ctLogin.md5(password);
				ingresa();

			}
		});
	}

	/*
	 * M�todo que consume el servicio web para comprobar los datos de inicio de
	 * sesi�n mientras muestra un mensaje informativo
	 */
	public void ingresa() {
		/* muestra ventana de progreso mientras se inicia sesi�n */
		ventanaProgreso = ProgressDialog.show(this, "", "Iniciando Sesi�n...",
				true);
		/* Creaci�n de hilo de ejecuci�n */
		new Thread(new Runnable() {

			@Override
			public void run() {
				/* Inicializaci�n de la clase de Servicios Web */
				ServicioWebSoap sw = new ServicioWebSoap();
				/* mensaje que capturar� los datos */
				Message msg = manejador.obtainMessage();
				/* consumo de servicio web de inicio de sesi�n */
				msg.obj = sw.iniciaSesion(nombreUsuario, pass);
				/* Envío de los datos obtenidos */
				manejador.sendMessage(msg);
			}
		}).start();
	}

	/* M�todo que procesa la respuesta al momento de intentar iniciar sesi�n */
	public void procesaRespuesta(String[] resultadoSesion) {
		/* Verificaci�n de la respuesta del servicio web */
		if (resultadoSesion[0].equals("OK")) {
			/* eliminaci�n de los valores de los campos de texto */
			ctLogin.setUsuario("");
			ctLogin.setPass("");
			ctLogin.setMensaje("");
			/* Creaci�n de Intent para pasar al menu principal de la aplicaci�n */
			Intent i = new Intent(ctLogin.getContext(),
					menuPrincipalActivity.class);
			i.putExtra("idUsuario", resultado[2]);
			startActivity(i);
		} else {
			ctLogin.setMensaje(resultadoSesion[1]);
		}
	}
}