package mx.itesm.fjzt;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


public abstract class Enemigo extends Sprite {

    protected World mundo;
    protected nivel1 nivel1;
    public Body cuerpo;

    public Enemigo(nivel1 nivel, float x, float y){
        this.mundo = nivel.getMundo();
        this.nivel1 = nivel;
        setPosition(x,y);
        crearEnemigo();
    }

    protected abstract void crearEnemigo();

}

