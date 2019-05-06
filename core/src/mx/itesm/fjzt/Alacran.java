package mx.itesm.fjzt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import static mx.itesm.fjzt.Pantalla.PX;

public class Alacran extends Enemigo {

    private float tiempo;
    private Animation<TextureRegion> correr;
    private Array<TextureRegion> frame;

    private boolean aDestruir;
    private boolean destruido;

    public Alacran(nivel1 nivel, float x, float y) {
        super(nivel, x, y);
        frame = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++){
            frame.add(new TextureRegion(nivel.getAtlas().findRegion("Linea-Alacran"),4+(150*i),20,150,50));
        }
        correr = new Animation(.4f,frame);
        tiempo = 0;
        setBounds(getX(),getY(),150/PX,50/PX);

        aDestruir = false;
        destruido = false;
    }

    @Override
    protected void defineEnemigo() {

    }

    public void update(float dt){
        tiempo += dt;

        //Checar si destruido el enemigo
        if(aDestruir && !destruido){
            mundo.destroyBody(cuerpo);
            destruido = true;
            //enemigo muerto imagen
        }
        else{
            cuerpo.setLinearVelocity(velocity);
            //posicion
            setPosition(cuerpo.getPosition().x - getWidth()/2 , cuerpo.getPosition().y - getHeight() / 2 );
            setRegion(correr.getKeyFrame(tiempo, true));
        }

    }


}
