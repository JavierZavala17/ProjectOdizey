package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

class checaColisiones implements ContactListener {

    private boolean squareVivo = true;
    int x = 0;

    @Override
    public void beginContact(Contact contact) {

        Fixture fixture1 = contact.getFixtureA(); //jugador
        Fixture fixture2 = contact.getFixtureB(); //

        if(fixture1.getUserData() == "jugador" || fixture2.getUserData() == "jugador"){
            Gdx.app.log("num + " + x++,"colision");
        }


        //System.out.print(fixture1.getBody().getType() + "pego con" + fixture2.getBody().getType());

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
