package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
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

public class PantallaWin extends Pantalla {

    private final JuegoDemo juego;

    private Texture textFondo;

    //Sprite de engrane grande
    private Sprite spriteEngraneGrande;
    private Texture textEngraneGrande;

    //Sprite de Cuadrado
    private Sprite spriteCuadro;
    private Texture textureCuadro;

    //Sprite de manecilla
    private Sprite spriteManecilla;
    private Texture textureManecilla;

    //MENU, Escenas, Independiente de la cámara(movimiento)
    private Stage escenaMenu; //Botones


    public PantallaWin (JuegoDemo juego) {
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

        textFondo = new Texture("PantallaAjustesFondo.png");

        textureCuadro = new Texture("CuadroGanar.png");
        spriteCuadro = new Sprite(textureCuadro);

        //ENGRANE GRANDE
        textEngraneGrande = new Texture("EngraneGrandeMenu.png");
        spriteEngraneGrande = new Sprite(textEngraneGrande);

        //MANECILLA
        textureManecilla = new Texture("Manecilla.png");
        spriteManecilla = new Sprite(textureManecilla);

        //Menú
        crearMenu();
        //Pasamos el control de INPUT a la escena
        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(false);
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //Botón BACK
        Texture textBtnDontEnter = new Texture("btnMenu.png");
        TextureRegionDrawable trdBtnDontEnter = new TextureRegionDrawable(new TextureRegion(textBtnDontEnter));

        Texture textBtnDontEnterR = new Texture("btnMenu2.png");
        TextureRegionDrawable trdBtnDontEnterR = new TextureRegionDrawable(new TextureRegion(textBtnDontEnterR));

        ImageButton btnDontEnter = new ImageButton(trdBtnDontEnter, trdBtnDontEnterR);
        btnDontEnter.setPosition(500-btnDontEnter.getWidth()/2,170);
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

        //Botón Ajustes
        Texture textBtnAjustes = new Texture("btnContinuar.png");
        TextureRegionDrawable trdBtnAjustes = new TextureRegionDrawable(new TextureRegion(textBtnAjustes));

        Texture textBtnAjustesS = new Texture("btnCOntinuar2.png");
        TextureRegionDrawable trdBtnAjustedsS = new TextureRegionDrawable(new TextureRegion(textBtnAjustesS));

        ImageButton btnHelp = new ImageButton(trdBtnAjustes, trdBtnAjustedsS);
        btnHelp.setPosition(800-btnHelp.getWidth()/2,170);
        // CARGAR LA PANTALLA DE MAPAS
        btnHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new PantallaSeleccionNivel(juego));
            }
        });
        escenaMenu.addActor(btnHelp);
    }

    @Override
    public void render(float delta) {
        // Dibujar (60 fps)
        Gdx.gl.glClearColor(0,0,0,1); // Color negro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Borra_todo_alv

        batch.setProjectionMatrix(camera.combined);// ESCALA

        batch.begin();

        batch.draw(textFondo,0,0);

        //COORDENADAS ENGRANE GRANDE
        spriteEngraneGrande.setX(642 - spriteEngraneGrande.getWidth()/2);
        spriteEngraneGrande.setY(339 - spriteEngraneGrande.getHeight()/2);
        spriteEngraneGrande.rotate(-0.3f);
        spriteEngraneGrande.draw(batch);

        //MANECILLA
        spriteManecilla.setX(642 -spriteManecilla.getWidth()/2);
        spriteManecilla.setY(339 - spriteManecilla.getHeight()/2);
        spriteManecilla.rotate(0.65f);
        spriteManecilla.draw(batch);

        //COORDENADAS CUADRO
        spriteCuadro.setX(0);
        spriteCuadro.setY(0);
        spriteCuadro.draw(batch);

        batch.end();

        escenaMenu.draw();
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
    public void dispose() {
        batch.dispose();
    }
}
