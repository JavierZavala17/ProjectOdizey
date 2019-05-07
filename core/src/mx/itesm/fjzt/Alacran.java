package mx.itesm.fjzt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        cuerpo = mundo.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ Pantalla.PX );

        //asignar bit
        fdef.filter.categoryBits = Pantalla.BIT_ENEMIGO;
        fdef.filter.maskBits = Pantalla.BIT_BALA | Pantalla.BIT_ENEMIGO| Pantalla.BIT_JUGADOR | Pantalla.BIT_PAREDES_ENEMIGOS| Pantalla.BIT_SUELO;

        fdef.shape = shape;
        cuerpo.createFixture(fdef).setUserData(this);
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

    public void draw(Batch batch) {
        //eliminar alacran muerto luego de 1 segundo
        if(!destruido || tiempo < 1){
            super.draw(batch);
        }
    }

    public void impactado(){
        aDestruir = true;
    }


}
