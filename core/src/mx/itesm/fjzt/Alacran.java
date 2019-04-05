package mx.itesm.fjzt;

import com.badlogic.gdx.graphics.Texture;
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


    public Alacran(mx.itesm.fjzt.nivel1 nivel, float x, float y) {
        super(nivel, x, y);
        frame = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++){
            frame.add(new TextureRegion(nivel.getAtlas().findRegion("Linea-Alacran"),4+(150*i),20,150,50));
        }
        correr = new Animation(.4f,frame);
        tiempo = 0;
        setBounds(getX(),getY(),150/PX,50/PX);
    }

    public void update(float dt){
        tiempo += dt;
        setPosition(cuerpo.getPosition().x - getWidth()/2 , cuerpo.getPosition().y - getHeight() / 2 );
        setRegion(correr.getKeyFrame(tiempo, true));
    }

    @Override
    protected void crearEnemigo() {
        BodyDef def = new BodyDef();
        def.position.set(getX(),getY());
        def.type = BodyDef.BodyType.StaticBody;
        cuerpo = mundo.createBody(def);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.4f,.3f);

        fdef.shape = shape;
        cuerpo.createFixture(fdef);

    }
}
