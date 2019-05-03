package mx.itesm.fjzt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mx.itesm.fjzt.PantallaCargando;

public class JuegoDemo extends Game {

	public SpriteBatch batch;

	@Override
	public void create(){
		batch = new SpriteBatch();
		setScreen(new PantallaCargando(this));
	}

	@Override
	public void render() {
		super.render();
	}
}
