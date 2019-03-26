package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Personaje {

    private Animation animacion;
    private Sprite sprite;
    private float timerAnimacion;

    public Personaje(float x, float y){
        //Cargar textura
        Texture textura = new Texture("marioFrames.png");
        //Crea una region
        TextureRegion region = new TextureRegion(textura);

        //Divide la region en frames de 32x64
        TextureRegion[][] textturapersonaje = region.split(50,66);
        animacion = new Animation(0.15f,textturapersonaje[0][1],textturapersonaje[0][2],textturapersonaje[0][3]);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;
        //Quieto
        sprite = new Sprite((textturapersonaje[0][0]));
        sprite.setPosition(64,64);
    }

    public void render(SpriteBatch batch){
        timerAnimacion += Gdx.graphics.getDeltaTime();
        TextureRegion region = (TextureRegion) animacion.getKeyFrame(timerAnimacion);
        batch.draw(region,sprite.getX(),sprite.getY());
    }

    public void moverX(float dx){
        sprite.setX(sprite.getX()+dx);
    }

    public float getX() { return sprite.getX(); }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public void setX(float x) {
        sprite.setX(x);
    }

    public void setY(float y) {
        sprite.setY(y);
    }
}
