package mx.itesm.demo;

import com.badlogic.gdx.Game;

public class Juego extends Game {


	//Dimensiones mundo virtual
	public static final float ANCHO= 1280;
	public static final float ALTO= 720;


	@Override
	public void create() {
		setScreen(new PantallaMenu(this)); //referencia del administracion  para pasar de pantalla a otra

	}
}
