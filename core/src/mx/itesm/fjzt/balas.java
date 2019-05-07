package mx.itesm.fjzt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class balas extends Sprite {

    nivel1 juego;
    World mundo;

    float tiempoVida;

    boolean disparoDer;
    boolean destruido;
    boolean aDestruir;

    Array<TextureRegion> frames;
    Animation animacion;

    Body cuerpo;

    public balas(nivel1 juego, float x, float y, boolean disparoDer){
        this.disparoDer = disparoDer;
        this.juego = juego;
        this.mundo = juego.getMundo();

        //Añadir el sprite de bala
        frames = new Array<TextureRegion>();
        for (int i = 1 ; i < 4; i++) {                              //Modificar los tamaños
            frames.add(new TextureRegion(juego.getAtlas().findRegion(""), i * 8, 0, 8, 8));
        }
        
        setBounds(x,y,6/Pantalla.PX,6/Pantalla.PX);
        definirBala();
    }

    private void definirBala() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(disparoDer ? getX() + 12 /Pantalla.PX : getX() - 12 / Pantalla.PX, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!mundo.isLocked())
            cuerpo = mundo.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / Pantalla.PX);
        fdef.filter.categoryBits = Pantalla.BIT_BALA;
        fdef.filter.maskBits = Pantalla.BIT_SUELO| Pantalla.BIT_ENEMIGO ;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        cuerpo.createFixture(fdef).setUserData(this);
        cuerpo.setLinearVelocity(new Vector2(disparoDer? 2 : -2, 2.5f));

    }

    public void update(float dt){
        tiempoVida += dt;
        setPosition(cuerpo.getPosition().x - getWidth()/2, cuerpo.getPosition().y-getHeight()/2);
        if((tiempoVida > 3 || aDestruir) && !destruido){
            mundo.destroyBody(cuerpo);
            destruido = true;
        }
        if(cuerpo.getLinearVelocity().y > 2f){
            cuerpo.setLinearVelocity(cuerpo.getLinearVelocity().x, 2f);
        }
        if(disparoDer && cuerpo.getLinearVelocity().x < 0 || (!disparoDer && cuerpo.getLinearVelocity().x > 0)){
            aDestruir();
        }

}

    private void aDestruir() {
        aDestruir = true;
    }

    public boolean isDestruido() {
        return destruido;
    }
}
