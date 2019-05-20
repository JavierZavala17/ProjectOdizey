package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class InterfazJugador implements Disposable {

    public Stage stage;
    //private Viewport vistaInterfaz;

    private float tiempoActual;
    public static Integer tiempoMundo;

    //private static Label countdownLabel;
    //private Label tiempoLabel;
    //private Label levelLabel;
    //private JuegoDemo juego;




    private boolean tiempoAcabo; //Verdadero en 0

    public InterfazJugador(SpriteBatch batch){


        tiempoMundo = 100; //40 0riginal
        tiempoActual = 0;

        /*vistaInterfaz = new FitViewport(PantallaCargando.ANCHO, PantallaCargando.ALTO, new OrthographicCamera());
        stage = new Stage(vistaInterfaz, batch);

        Table tabla = new Table();
        tabla.top();
        tabla.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", tiempoMundo), new LabelStyle(new BitmapFont(), Color.WHITE));
        tiempoLabel = new Label("TIEMPO", new LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("NIVEL: 1", new LabelStyle(new BitmapFont(), Color.WHITE));



        tabla.add(levelLabel).expandX().padTop(5);
        tabla.add(tiempoLabel).expandX().padTop(5);
        tabla.add(countdownLabel).expandX().padTop(5);

        stage.addActor(tabla);*/

    }

    public void update(float dt){
        tiempoActual += dt;
        if(tiempoActual >=1){
            if(tiempoMundo > 0){
                tiempoMundo--;
            }else{
                tiempoAcabo = true;
            }
            //countdownLabel.setText(String.format("%03d",tiempoMundo));
            tiempoActual = 0;

        }

    }

    public static void addTiempo(int value){
        tiempoMundo += value;
        //countdownLabel.setText(String.format("%06d",tiempoMundo));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTiempoAcabo(){
        return tiempoAcabo;
    }

    public String tiempoMundo(){
        return String.format("%03d", tiempoMundo);
    }
}
