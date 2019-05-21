package mx.itesm.fjzt;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

class checaColisiones implements ContactListener {


    @Override
    public void beginContact(Contact contact) {

        Fixture fixture1 = contact.getFixtureA();
        Fixture fixture2 = contact.getFixtureB();

        int colision = fixture1.getFilterData().categoryBits | fixture2.getFilterData().categoryBits;

        switch (colision) {
            case Pantalla.BIT_JUGADOR | Pantalla.BIT_WIN:
                //Gdx.app.log("","Gano");
                mapa1.ganar ++;
                break;
            case Pantalla.BIT_JUGADOR | Pantalla.BIT_ENEMIGO:
                if(fixture1.getFilterData().categoryBits == Pantalla.BIT_ENEMIGO){
                    if(mapa1.vidaPersonaje==3){
                        mapa1.vidaPersonaje -= 1f;
                        break;
                    }else if(mapa1.vidaPersonaje==2 && mapa1.invunerabilidad > 0){
                        mapa1.vidaPersonaje -= 1f;
                        break;
                    }else if(mapa1.vidaPersonaje==1&& mapa1.invunerabilidad > 0){
                        mapa1.ganar -= 1;
                    }
                }else{
                    if(mapa1.vidaPersonaje==3){
                        mapa1.vidaPersonaje -= 1f;
                        break;
                    }else if(mapa1.vidaPersonaje==2&& mapa1.invunerabilidad > 0){
                        mapa1.vidaPersonaje -= 1f;
                        break;
                    }else if(mapa1.vidaPersonaje==1&& mapa1.invunerabilidad > 0){
                        mapa1.ganar -= 1;
                    }
                }
                mapa1.invunerabilidad = 250;
                //Gdx.app.log("","score" + mapa1.ganar);
                break;
            case Pantalla.BIT_JUGADOR | Pantalla.BIT_OBJETOS:
                if(fixture1.getFilterData().categoryBits == Pantalla.BIT_ENEMIGO){
                    if(Reloj.usos == 5){
                        InterfazJugador.addTiempo(10);
                        Reloj.usos --;
                    } else if(Reloj.usos == 4) {
                        InterfazJugador.addTiempo(10);
                        Reloj.usos--;
                    }else if(Reloj.usos == 3) {
                        InterfazJugador.addTiempo(10);
                        Reloj.usos--;
                    }else if(Reloj.usos == 2) {
                        InterfazJugador.addTiempo(10);
                        Reloj.usos--;
                    }else if(Reloj.usos == 1) {
                        InterfazJugador.addTiempo(10);
                        Reloj.usos--;
                    }
                }else{
                    if(Reloj.usos == 5){
                        InterfazJugador.addTiempo(10);
                        Reloj.usos --;
                    } else if(Reloj.usos == 4) {
                        InterfazJugador.addTiempo(10);
                        Reloj.usos--;
                    }else if(Reloj.usos == 3) {
                        InterfazJugador.addTiempo(10);
                        Reloj.usos--;
                    }else if(Reloj.usos == 2) {
                        InterfazJugador.addTiempo(10);
                        Reloj.usos--;
                    }else if(Reloj.usos == 1) {
                        InterfazJugador.addTiempo(10);
                        Reloj.usos--;
                    }
                }
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
