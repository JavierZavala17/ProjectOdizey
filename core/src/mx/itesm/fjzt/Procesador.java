package mx.itesm.fjzt;
import com.badlogic.gdx.InputAdapter;

public class Procesador extends InputAdapter {

    @Override                   //Donde hemos tocado   // Que hemos tocado  //
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        System.out.println("Tocado en posicion X: " + screenX + ", y: " + (-1*screenY + 720)  );
        //System.out.println("Dedo " + pointer + " y el boton " + button);
        return true;
    }


    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        return super.touchUp(screenX,screenY,pointer, button);
    }

}