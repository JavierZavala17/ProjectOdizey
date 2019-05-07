package mx.itesm.fjzt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mx.itesm.fjzt.PantallaCargando;

public class JuegoDemo extends Game {

    public SpriteBatch batch;
    private final AssetManager assetManager;
    public Music musicaFondo;

    public JuegoDemo(){
        assetManager = new AssetManager();
    }

    @Override
    public void create(){
        batch = new SpriteBatch();
        setScreen(new PantallaCargando(this));
        cargarMusica();
        iniciarMusica();
    }
    // MUSICA
    public void cargarMusica() {
        AssetManager manager = new AssetManager();
        manager.load("MenuMusic.mp3", Music.class);
        manager.finishLoading();    // síncrono
        musicaFondo = manager.get("MenuMusic.mp3");

    }

    public void iniciarMusica(){
        musicaFondo.play();
    }

    public void pausarMusica(){
        musicaFondo.stop();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.clear();
    }
}
