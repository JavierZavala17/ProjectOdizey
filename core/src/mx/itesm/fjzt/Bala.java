package mx.itesm.fjzt;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bala {

    private float velocidadY;
    private Texture textProyectil;
    private Sprite sprite;

    public Bala (float x,float y){
        textProyectil = new Texture("bala.png");
        sprite = new Sprite(textProyectil);
        sprite.setPosition(x,y);
        sprite.setColor(0,1,0,1);
        velocidadY = PantallaCargando.ALTO / 3;
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        sprite.rotate(5);
    }

    // FÍSICA
    public void moverY(float dt) {
        float dy = velocidadY * dt;
        sprite.setY(sprite.getY() + dy);
    }

    public Sprite getSprite(){
        return sprite;
    }
}
