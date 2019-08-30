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
				/* definición del valor de usuario */
				nombreUsuario = usuario;
				/* Encriptación del password a algoritmo MD5 */
				pass = ctLogin.md5(password);
				ingresa();

			}
		});
	}

	/*
	 * Método que consume el servicio web para comprobar los datos de inicio de
	 * sesión mientras muestra un mensaje informativo
	 */
	public void ingresa() {
		/* muestra ventana de progreso mientras se inicia sesión */
		ventanaProgreso = ProgressDialog.show(this, "", "Iniciando Sesión...",
				true);
		/* Creación de hilo de ejecución */
		new Thread(new Runnable() {

			@Override
			public void run() {
				/* Inicialización de la clase de Servicios Web */
				ServicioWebSoap sw = new ServicioWebSoap();
				/* mensaje que capturará los datos */
				Message msg = manejador.obtainMessage();
				/* consumo de servicio web de inicio de sesión */
				msg.obj = sw.iniciaSesion(nombreUsuario, pass);
				/* EnvÃ­o de los datos obtenidos */
				manejador.sendMessage(msg);
			}
		}).start();
	}

	/* Método que procesa la respuesta al momento de intentar iniciar sesión */
	public void procesaRespuesta(String[] resultadoSesion) {
		/* Verificación de la respuesta del servicio web */
		if (resultadoSesion[0].equals("OK")) {
			/* eliminación de los valores de los campos de texto */
			ctLogin.setUsuario("");
			ctLogin.setPass("");
			ctLogin.setMensaje("");
			/* Creación de Intent para pasar al menu principal de la aplicación */
			Intent i = new Intent(ctLogin.getContext(),
					menuPrincipalActivity.class);
			i.putExtra("idUsuario", resultado[2]);
			startActivity(i);
		} else {
			ctLogin.setMensaje(resultadoSesion[1]);
		}
	}
}