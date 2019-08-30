package com.workflow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class menuPrincipalActivity extends Activity {
	/* Variables */
	ImageButton datosUsuario;
	ImageButton solicitudes;
	ImageButton cambioPass;
	ImageButton salida;
	private static final int CODIGO_PETICION = 11;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.menu_principal);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		final String idUsuario = extras.getString("idUsuario");
		if (idUsuario != null) {
			/* Captura de botones */
			datosUsuario = (ImageButton) findViewById(R.id.cambiarDatos);
			solicitudes = (ImageButton) findViewById(R.id.solicitudes);
			cambioPass = (ImageButton) findViewById(R.id.pass);
			salida = (ImageButton) findViewById(R.id.salir);
			/*
			 * Sobreescritura del evento click del boton de actualizar datos de
			 * usuario
			 */
			datosUsuario.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent ventanaDatos = new Intent(getApplicationContext(),
							actualizarDatosActivity.class);
					ventanaDatos.putExtra("idUsuario", idUsuario);
					startActivityForResult(ventanaDatos, CODIGO_PETICION);
				}
			});
			/*
			 * Sobreescritura del evento click del boton de la lista de
			 * solicitudes
			 */
			solicitudes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent ventanaSolicitudes = new Intent(
							getApplicationContext(),
							ListaSolicitudesActivity.class);
					ventanaSolicitudes.putExtra("idUsuario", idUsuario);
					startActivity(ventanaSolicitudes);
				}
			});
			/*
			 * Sobreescritura del evento click del boton del cambio de
			 * contraseña
			 */
			cambioPass.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent actualizaClave = new Intent(getApplicationContext(),
							ActualizaClaveActivity.class);
					actualizaClave.putExtra("idUsuario", idUsuario);
					startActivityForResult(actualizaClave, CODIGO_PETICION);
				}
			});
			/*
			 * Sobreescritura del evento click del boton de salida
			 */
			salida.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int codigoPeticion, int codigoResultado,
			Intent datos) {
		if (codigoResultado == RESULT_OK && codigoPeticion == CODIGO_PETICION) {
			if (datos.hasExtra("resultadoActividad")) {
				/*
				 * Captura del mensaje de respuesta de la actividad que se
				 * convocó
				 */
				String mensaje = datos.getExtras().getString(
						"resultadoActividad");
				/* Mensaje en pantalla */
				Toast.makeText(getApplicationContext(), mensaje,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle("Salida")
					.setMessage("Esta seguro de querer salir ?")
					.setNegativeButton("Cancelar", null)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
