package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class PantallaMasInfo extends Pantalla {

    private final JuegoDemo juego;

    private Texture textFondo;

    //Sprite de engrane mediano
    private Sprite spriteEngraneMediano;
    private Texture textureEngraneMediano;

    //MENU, Escenas, Independiente de la cámara(movimiento)
    private Stage escenaMenu; //Botones


    public PantallaMasInfo (JuegoDemo juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        // ~ Constructor
        camera = new OrthographicCamera(ANCHO, PantallaCargando.ALTO);
        camera.position.set(ANCHO/2,PantallaCargando.ALTO/2,0);
        camera.update();
        // Vista
        vista = new StretchViewport(ANCHO,PantallaCargando.ALTO,camera);
        batch = new SpriteBatch();

        textFondo = new Texture("PantallaMasInfoFondo.png");

        //ENGRANE MEDIANO
        textureEngraneMediano = new Texture("EngraneMedianoMenu.png");
        spriteEngraneMediano = new Sprite(textureEngraneMediano);

        //Menú
        crearMenu();
        //Pasamos el control de INPUT a la escena
        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(true);

    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //Botón BACK
        Texture textBtnDontEnter = new Texture("btnRegresar.png");
        TextureRegionDrawable trdBtnDontEnter = new TextureRegionDrawable(new TextureRegion(textBtnDontEnter));

        Texture textBtnDontEnterR = new Texture("btnRegresar2.png");
        TextureRegionDrawable trdBtnDontEnterR = new TextureRegionDrawable(new TextureRegion(textBtnDontEnterR));

        ImageButton btnDontEnter = new ImageButton(trdBtnDontEnter, trdBtnDontEnterR);
        btnDontEnter.setPosition(50,510);
        // CARGAR LA PANTALLA DE MAPAS
        btnDontEnter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaMenu.addActor(btnDontEnter);
    }

    @Override
    public void render(float delta) {
        // Dibujar (60 fps)
        Gdx.gl.glClearColor(0,0,0,1); // Color negro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Borra_todo_alv

        batch.setProjectionMatrix(camera.combined);// ESCALA

        batch.begin();

        batch.draw(textFondo,0,0);

        //COORDENADAS ENGRANE MEDIANO
        spriteEngraneMediano.setX(1230 - spriteEngraneMediano.getWidth()/2);
        spriteEngraneMediano.setY(50- spriteEngraneMediano.getHeight()/2);
        spriteEngraneMediano.rotate(0.35f);
        spriteEngraneMediano.draw(batch);

        batch.end();

        escenaMenu.draw();
        teclaBack();
    }

    private void teclaBack() {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            juego.setScreen(new PantallaMenu(juego));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        textFondo.dispose();
    }
}
