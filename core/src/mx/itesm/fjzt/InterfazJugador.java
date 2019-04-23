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
    private Viewport vistaInterfaz;

    private float tiempoActual;
    private Integer tiempoMundo;

    private Label countdownLabel;
    private Label tiempoLabel;
    private Label levelLabel;
    private JuegoDemo juego;

    public InterfazJugador(SpriteBatch batch){
        tiempoMundo = 40;
        tiempoActual = 0;

        vistaInterfaz = new FitViewport(PantallaCargando.ANCHO, PantallaCargando.ALTO, new OrthographicCamera());
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

        stage.addActor(tabla);

    }

    public void update(float dt){
        tiempoActual += dt;
        if(tiempoActual >=1){
            if(tiempoMundo > 0){
                tiempoMundo--;
            }else{
                //
            }
            countdownLabel.setText(String.format("%03d",tiempoMundo));
            tiempoActual = 0;

        }

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
