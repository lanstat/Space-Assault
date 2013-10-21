package dev.andcamp.spaceassault.scenes;

import android.os.Bundle;

public interface IStateListener {
	
	/**
	 * Se debe usar para agregar hilos asincronos a la aplicacion
	 * @param arguments Bundle de parametros para la escena
	 */
	public void initialize(Bundle arguments);
	
	/**
	 * Funcion que se encarga de cargar los recursos de la escena
	 */
	public void loadResources();
	
	/**
	 * Funcion que se encarga de crearlos elementos de la escene, ej. sprites, botones, etc.
	 */
	public void createScene();
	
	/**
	 * Funcion que se encarga de eliminar los hilos creados en la escena
	 */
	public void destroy();
}
