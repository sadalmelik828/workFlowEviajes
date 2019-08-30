package com.workflow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ControlLogin extends LinearLayout {
	/* variables */
	private EditText cajaUsuario, cajaPass;
	private Button BtIniciaSesion;
	private TextView mensaje;
	private LoginListener listener;
	
	/* Constructores */
	public ControlLogin(Context contexto) {
		super(contexto);
		inicializar();
	}
	
	public ControlLogin(Context contexto, AttributeSet atributos) {
		super(contexto, atributos);
		inicializar();
	}
	
	/* ************* Métodos ************* */
	/* Método que se encarga de inicializar la clase */
	private void inicializar() {
		/* interfaz de control */
		String is = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater)getContext().getSystemService(is);
		li.inflate(R.layout.control_login, this, true);
		
		/* referencias a los controles */
		cajaUsuario = (EditText)findViewById(R.id.CajaUsuario);
		cajaPass = (EditText)findViewById(R.id.CajaPass);
		BtIniciaSesion = (Button)findViewById(R.id.BtIniciaSesion);
		mensaje = (TextView)findViewById(R.id.Mensaje);
		
		/* Asociar Eventos */
		asignarEventos();
	}
	
	/* Método que define la interfaz del listener */
	public void setLoginListener(LoginListener l) {
		listener = l;
	}
	
	/* Método que define la personalizacion del evento */
	private void asignarEventos() {
		BtIniciaSesion.setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						listener.iniciarSesion(cajaUsuario.getText().toString(), cajaPass.getText().toString());
					}
				}
		);
	}
	
	/* Método que define el mensaje a mostrar cuando se intenta iniciar sesion */
	public void setMensaje(String msg) {
		mensaje.setText(msg);
	}
	
	/* Método que define el valor del nombre de usuario en el campo de texto respectivo */
	public void setUsuario(String nombreUsuario) {
		cajaUsuario.setText(nombreUsuario);
	}
	
	/* Método que define el valor del password en el campo de texto respectivo */
	public void setPass(String pass) {
		cajaPass.setText(pass);
	}
	
	/* Método para encriptar contraseña de inicio de sesión en hash md5 */
	public String md5(String s) {
		/* Variables */
		String encriptacion = ""; 
	    try {
	        // Creación de MD5 Hash
	        MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        md.update(s.getBytes());
	        byte b[] = md.digest();
	        int longitud = b.length;
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer(longitud);
	        for (int i = 0; i < longitud; i++) {
	        	int u = b[i] & 255;
	        	if (u < 16) {
	        		hexString.append("0" + Integer.toHexString(u));
	        	} else {
	        		hexString.append(Integer.toHexString(u));
	        	}
	        }
	        encriptacion = hexString.toString();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return encriptacion;
	}
}
