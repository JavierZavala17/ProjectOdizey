package mx.itesm.fjzt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mx.itesm.fjzt.PantallaCargando;

public class JuegoDemo extends Game {

    private final AssetManager assetManager;
    private Preferences preferences;
    public SpriteBatch batch;

    public JuegoDemo(){
        assetManager = new AssetManager();
    }

    @Override
    public void create(){
        batch = new SpriteBatch();
        setScreen(new PantallaCargando(this));
        preferences = Gdx.app.getPreferences(JuegoDemo.class.getName());
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Preferences getPreferences(){
        return preferences;
    }
}
