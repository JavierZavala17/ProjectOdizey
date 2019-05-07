package mx.itesm.fjzt;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Fixture;

public class reloj extends items {


    public reloj(nivel1 nivel, MapObject objecto) {
        super(nivel, objecto);
        fix.setUserData(this);
        setCategoryFilter(Pantalla.BIT_OBJETOS);
    }

    @Override
    public void hit() {
        setCategoryFilter(Pantalla.BIT_DESTRUIDO);
        getCell().setTile(null);
        InterfazJugador.addTiempo(5);
        //Musica para que suene

    }
}
