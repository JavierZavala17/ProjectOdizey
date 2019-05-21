package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

class checaColisiones2 implements ContactListener {


    @Override
    public void beginContact(Contact contact) {

        Fixture fixture1 = contact.getFixtureA();
        Fixture fixture2 = contact.getFixtureB();

        int colision = fixture1.getFilterData().categoryBits | fixture2.getFilterData().categoryBits;

        switch (colision) {
            case Pantalla.BIT_JUGADOR | Pantalla.BIT_WIN:
                Gdx.app.log("","Gano");
                mapa2.ganar ++;
                break;
            case Pantalla.BIT_JUGADOR | Pantalla.BIT_ENEMIGO:
                if(fixture1.getFilterData().categoryBits == Pantalla.BIT_ENEMIGO){
                    if(mapa2.vidaPersonaje==3){
                        mapa2.vidaPersonaje -= .5f;
                        break;
                    }else if(mapa2.vidaPersonaje==2){
                        mapa2.vidaPersonaje -= .5f;
                        break;
                    }else if(mapa2.vidaPersonaje==1){
                        mapa2.ganar -= 1;
                    }
                }else{
                    if(mapa2.vidaPersonaje==3){
                        mapa2.vidaPersonaje -= .5f;
                        break;
                    }else if(mapa2.vidaPersonaje==2){
                        mapa2.vidaPersonaje -= .5f;
                        break;
                    }else if(mapa2.vidaPersonaje==1){
                        mapa2.ganar -= 1;
                    }
                }
                //Gdx.app.log("","score" + mapa1.ganar);
                break;
            case Pantalla.BIT_JUGADOR | Pantalla.BIT_OBJETOS:
                InterfazJugador.addTiempo(10);
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


}
