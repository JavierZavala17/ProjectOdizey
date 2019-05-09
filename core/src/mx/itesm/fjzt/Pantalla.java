package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Representa comportamiento genérico de cualquier pantalla que forma
 * parte del juego
 */

public abstract class Pantalla implements Screen
{
    // Atributos disponibles en todas las clases del proyecto
    public static final float ANCHO = 1280;
    public static final float ALTO = 720;
    public static final float PX = 100;


    public static final short BIT_JUGADOR = 1;
    public static final short BIT_ENEMIGO = 2;
    public static final short BIT_OBJETOS = 4;
    public static final short BIT_BALA = 8;
    public static final short BIT_PAREDES_ENEMIGOS = 16;
    public static final short BIT_WIN = 32;
    public static final short BIT_SUELO = 64;
    public static final short BIT_ZILO = 128;
    public static final short BIT_DESTRUIDO = 256;

    // Atributos disponibles solo en las subclases
    // Todas las pantallas tienen una cámara y una vista
    protected OrthographicCamera camera;
    protected Viewport vista;
    // Todas las pantallas dibujan algo :)
    protected SpriteBatch batch;
    //Todas las pantallas usan las preferencias
    protected Preferences preferencias;

    //Todas las pantallas usan musica y juego
    private final JuegoDemo juego;
    private final AssetManager assetManager; // = new AssetManager();
    public boolean musicaMenus;
    public boolean MUSIC_VOLUME_DEFAULT = true;
    protected static Music music;

    public Pantalla(JuegoDemo juego) {
        this.juego = juego;
        assetManager = new AssetManager();
        // Crea la cámara con las dimensiones del mundo
        camera = new OrthographicCamera(ANCHO, ALTO);
        // En el centro de la pantalla
        camera.position.set(ANCHO / 2, ALTO / 2, 0);
        camera.update();
        // La vista que escala los elementos gráficos
        vista = new StretchViewport(ANCHO, ALTO, camera);
        // El objeto que administra los trazos gráficos
        batch = new SpriteBatch();
    }

    // Borra la pantalla con fondo negro
    protected void borrarPantalla() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    protected void cargarMusica(){
        assetManager.load("MenuMusic.mp3", Music.class);
        assetManager.finishLoading();
        music = assetManager.get("MenuMusic.mp3");
        music.play();
    }

    protected void savePreferences() {
        preferencias.putBoolean("musicaMenu", musicaMenus);
        preferencias.flush();
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void hide() {
        // Libera los recursos asignados por cada pantalla
        // Las subclases están obligadas a sobrescribir el método dispose()
        dispose();
    }
}