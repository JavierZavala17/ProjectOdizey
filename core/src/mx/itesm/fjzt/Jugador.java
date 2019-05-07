package mx.itesm.fjzt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static mx.itesm.fjzt.Pantalla.PX;

public class Jugador extends Sprite {

    public enum estados {saltando, corriendo, muerto};

    public estados actual;
    public estados anterior;

    public World mundo;
    public Body cuerpo;

    private TextureRegion jugador0;

    private Animation<TextureRegion> jugadorSalta;
    private Animation<TextureRegion> jugadorCorre;

    private float tempo;
    private boolean corriendoD;


    private boolean jugadorMuerto;
    private boolean jugadorSaltando;

    //Opcional , vidas : private int vidas = 3;


    public Jugador(World world, nivel1 screen){
        super(screen.getAtlas().findRegion("Linea-Silo-Co"));
        this.mundo = world;

        actual = estados.corriendo;
        anterior = estados.corriendo;
        tempo = 0;
        corriendoD = true;

        //Correr
        Array<TextureRegion> frame = new Array<TextureRegion>();
        for(int i = 0; i< 12 ;i++){
            frame.add(new TextureRegion(getTexture(),8+(80*i),137,79,90));
        }
        jugadorCorre = new Animation(.2f,frame);
        frame.clear();

        //SALTAR
        for(int i = 4; i < 7; i++){
            frame.add(new TextureRegion(getTexture(),20+(80*i),137,79,90));
        }
        jugadorSalta = new Animation(.5f,frame);

        jugador0 = new TextureRegion(getTexture(),12,137,79,230 );

        defineJugador();
        setBounds( 12/PX,133/PX,79/PX,230/PX);
        setRegion(jugador0);
    }

    public void update(float dt){
        setPosition(cuerpo.getPosition().x - getWidth()/2, cuerpo.getPosition().y - getHeight()/2);
        setRegion(getCuadro(dt));
    }

    private TextureRegion getCuadro(float dt) {
        actual = getEstado();

        TextureRegion region = new TextureRegion();
        if(actual == estados.saltando){
            region = jugadorSalta.getKeyFrame(tempo);
        }
        if(actual == estados.corriendo){
            region = jugadorCorre.getKeyFrame(tempo,true);
        }
        if((cuerpo.getLinearVelocity().x < 0 || !corriendoD) && !region.isFlipX()){
            region.flip(true, false);
            corriendoD = false;
        }
        else if((cuerpo.getLinearVelocity().x > 0 || corriendoD) && region.isFlipX()){
            region.flip(true, false);
            corriendoD = true;
        }

        tempo = actual == anterior ? tempo + dt : 0;
        anterior = actual;
        return region;
    }

    private estados getEstado() {
        if((cuerpo.getLinearVelocity().y > 0 && actual == estados.saltando )|| (cuerpo.getLinearVelocity().y < 0 && anterior == estados.saltando) ){
            return estados.saltando;
        }
        return estados.corriendo;
    }

    private void defineJugador() {
        BodyDef def = new BodyDef();
        def.position.set(300/PX,360/PX);
        def.type = BodyDef.BodyType.DynamicBody;
        cuerpo = mundo.createBody(def);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.4f,1.1f);

        fdef.filter.categoryBits = Pantalla.BIT_ZILO;
        fdef.filter.maskBits =  Pantalla.BIT_ENEMIGO| Pantalla.BIT_JUGADOR | Pantalla.BIT_PAREDES_ENEMIGOS| Pantalla.BIT_SUELO  | Pantalla.BIT_WIN;

        fdef.shape = shape;
        cuerpo.createFixture(fdef).setUserData(this);
    }

    public boolean estaMuerto(){
        return jugadorMuerto;
    }

    public float getTempo() {
        return tempo;
    }

    public void saltar() {
        if(actual != estados.saltando){
            cuerpo.applyLinearImpulse(new Vector2(0,6.5f), cuerpo.getWorldCenter(),true);
            actual = estados.saltando;
        }
    }

}
