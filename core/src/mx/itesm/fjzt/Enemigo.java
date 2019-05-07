package mx.itesm.fjzt;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


public abstract class Enemigo extends Sprite {

    protected World mundo;
    protected nivel1 nivel1;
    public Body cuerpo;
    public Vector2 velocity;

    public Enemigo(nivel1 nivel, float x, float y){
        this.mundo = nivel.getMundo();
        this.nivel1 = nivel;
        setPosition(x,y);
        defineEnemigo();
        velocity = new Vector2(1,0);
        cuerpo.setActive(false); //stay there until wake up

    }

    protected abstract void defineEnemigo();

    public abstract void update(float dt);

    public abstract void impactado();

    public void reverseVelocity(boolean x, boolean y){
        if(x){
            velocity.x = -velocity.x;
        }
        if(y){
            velocity.y = -velocity.y;
        }
    }

}
//hola chicos
