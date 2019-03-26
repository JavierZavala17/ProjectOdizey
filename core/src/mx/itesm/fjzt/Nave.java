package mx.itesm.fjzt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Nave {
    private Texture textNave;
    private Sprite sprite;

    public Nave(float x,float y){
        textNave = new Texture("nave.png");
        sprite = new Sprite(textNave);
        sprite.setPosition(x,y);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }


    public void moverX(int dx) {
        sprite.setX(sprite.getX() + dx);
    }

    public Sprite getSprite(){
        return sprite;
    }

    public void disparar(){

    }
}
