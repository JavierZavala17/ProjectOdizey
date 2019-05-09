package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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


public class PantallaSeleccionNivel extends Pantalla {

    private final JuegoDemo juego;

    private Texture textFondo;

    //Sprite de engrane mediano
    private Sprite spriteEngraneMediano;
    private Texture textureEngraneMediano;

    //Sprite de engrane pequeño
    private Sprite spriteEngranePequeño;
    private Texture textureEngranePequeño;

    //MENU, Escenas, Independiente de la cámara(movimiento)
    private Stage escenaMenu; //Botones

    public PantallaSeleccionNivel(JuegoDemo juego) {
        super(juego);
        this.juego = juego;
        this.preferencias = juego.getPreferences();
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

        textFondo = new Texture("PantallaSeleccionNivelFondo.png");

        //ENGRANE MEDIANO
        textureEngraneMediano = new Texture("EngraneMedianoMenu.png");
        spriteEngraneMediano = new Sprite(textureEngraneMediano);

        //ENGRANE PEQUEÑO
        textureEngranePequeño = new Texture("EngraneChicoMenu.png");
        spriteEngranePequeño = new Sprite(textureEngranePequeño);

        //Menú
        crearMenu();

        //Pasamos el control de INPUT a la escena
        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(true);
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //Botón LEVEL 1
        Texture textBtnPlay = new Texture("btnNivel1.png");
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(textBtnPlay));

        Texture textBtnPlayP = new Texture("btnNivel1(2).png");
        TextureRegionDrawable trdBtnPlayP = new TextureRegionDrawable(new TextureRegion(textBtnPlayP));

        ImageButton btnPlay = new ImageButton(trdBtnPlay,trdBtnPlayP);
        btnPlay.setPosition(194,460);
        escenaMenu.addActor(btnPlay);
        //Agregar el LISTENER
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Responder al evento del boton
                musicaMenus = false;
                music.stop();
                savePreferences();
                juego.setScreen(new mapa1(juego));
            }
        });

        //Botón LEVEL 2
        Texture textBtnAjustes = new Texture("btnNivel2.png");
        TextureRegionDrawable trdBtnAjustes = new TextureRegionDrawable(new TextureRegion(textBtnAjustes));

        Texture textBtnAjustesS = new Texture("btnNivel2(2).png");
        TextureRegionDrawable trdBtnAjustedsS = new TextureRegionDrawable(new TextureRegion(textBtnAjustesS));

        ImageButton btnHelp = new ImageButton(trdBtnAjustes, trdBtnAjustedsS);
        btnHelp.setPosition(535,518);
        // CARGAR LA PANTALLA DE MAPAS
        btnHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new nivel1(juego));
            }
        });
        escenaMenu.addActor(btnHelp);

        //Botón LEVEL 3
        Texture textBtnAyuda = new Texture("btnNivel3.png");
        TextureRegionDrawable trdBtnAyuda = new TextureRegionDrawable(new TextureRegion(textBtnAyuda));

        Texture textBtnAyudaA = new Texture("btnNivel3(2).png");
        TextureRegionDrawable trdBtnAyudaA = new TextureRegionDrawable(new TextureRegion(textBtnAyuda));

        ImageButton btnAyuda = new ImageButton(trdBtnAyuda, trdBtnAyudaA);
        btnAyuda.setPosition(887,477);
        // CARGAR LA PANTALLA DE MAPAS
        btnAyuda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new mapa1(juego));
            }
        });
        escenaMenu.addActor(btnAyuda);

        //Botón BACK
        Texture textBtnDontEnter = new Texture("btnRegresar.png");
        TextureRegionDrawable trdBtnDontEnter = new TextureRegionDrawable(new TextureRegion(textBtnDontEnter));

        Texture textBtnDontEnterR = new Texture("btnRegresar2.png");
        TextureRegionDrawable trdBtnDontEnterR = new TextureRegionDrawable(new TextureRegion(textBtnDontEnterR));

        ImageButton btnDontEnter = new ImageButton(trdBtnDontEnter, trdBtnDontEnterR);
        btnDontEnter.setPosition(10,22);
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

        //Musica
        musicaMenus = preferencias.getBoolean("musicaMenu", MUSIC_VOLUME_DEFAULT);
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
        spriteEngraneMediano.setX(640 - spriteEngraneMediano.getWidth()/2);
        spriteEngraneMediano.setY(-50 - spriteEngraneMediano.getHeight()/2);
        spriteEngraneMediano.rotate(0.35f);
        spriteEngraneMediano.draw(batch);

        //COORDENADAS ENGRANE PEQUEÑO
        spriteEngranePequeño.setX(640 - spriteEngranePequeño.getWidth()/2);
        spriteEngranePequeño.setY(-50 - spriteEngranePequeño.getHeight()/2);
        spriteEngranePequeño.rotate(-0.37f);
        spriteEngranePequeño.draw(batch);

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
    public void dispose() {
        batch.dispose();
        textFondo.dispose();
        textureEngraneMediano.dispose();
        textureEngranePequeño.dispose();
    }
}

