package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.xml.soap.Text;

public class PantallaMenu implements Screen {

    private final JuegoDemo juego;
    // Camara del juego
    private OrthographicCamera camera;
    // Escalar
    private Viewport vista;
    // Optimizar los gráficos
    private SpriteBatch batch;

    private Texture textFondo;

    //Sprite
    private Sprite spriteSmash;
    private Texture textSmash;

    //MENU, Escenas, Independiente de la cámara(movimiento)
    private Stage escenaMenu; //Botones

    //Sprite de engrane grande
    private Sprite spriteEngraneGrande;
    private Texture textEngraneGrande;

    //Sprite de engrane mediano
    private Sprite spriteEngraneMediano;
    private Texture textureEngraneMediano;

    //Sprite de engrane pequeño
    private Sprite spriteEngranePequeño;
    private Texture textureEngranePequeño;



    public PantallaMenu(JuegoDemo juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        // ~ Constructor
        camera = new OrthographicCamera(PantallaCargando.ANCHO, PantallaCargando.ALTO);
        camera.position.set(PantallaCargando.ANCHO/2,PantallaCargando.ALTO/2,0);
        camera.update();
        // Vista
        vista = new StretchViewport(PantallaCargando.ANCHO,PantallaCargando.ALTO,camera);
        batch = new SpriteBatch();

        textFondo = new Texture("Menu.png");

        //ENGRANE GRANDE
        textEngraneGrande = new Texture("EngraneGrandeMenu.png");
        spriteEngraneGrande = new Sprite(textEngraneGrande);

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
        Gdx.input.setCatchBackKey(false);
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //Botón PLAY
        Texture textBtnPlay = new Texture("btnPlay.png");
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(textBtnPlay));

        Texture textBtnPlayP = new Texture("btnPlayP.png");
        TextureRegionDrawable trdBtnPlayP = new TextureRegionDrawable(new TextureRegion(textBtnPlayP));

        ImageButton btnPlay = new ImageButton(trdBtnPlay,trdBtnPlayP);
        btnPlay.setPosition(PantallaCargando.ANCHO/2 - btnPlay.getWidth()/2, PantallaCargando.ALTO*2/3f - btnPlay.getHeight()/2);
        escenaMenu.addActor(btnPlay);
        //Agregar el LISTENER
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new PantallaSpaceInvaders(juego));
            }
        });

        //Botón HELP
        Texture textBtnHelp = new Texture("btnHelp.png");
        TextureRegionDrawable trdBtnHelp = new TextureRegionDrawable(new TextureRegion(textBtnHelp));

        Texture textBtnHelpP = new Texture("btnHelpP.png");
        TextureRegionDrawable trdBtnHelpP = new TextureRegionDrawable(new TextureRegion(textBtnHelpP));

        ImageButton btnHelp = new ImageButton(trdBtnHelp, trdBtnHelpP);
        btnHelp.setPosition(PantallaCargando.ANCHO/2 - btnHelp.getWidth()/2, PantallaCargando.ALTO*1/3f - btnHelp.getHeight()/2);
        // CARGAR LA PANTALLA DE MAPAS
        btnHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaMapa(juego));
            }
        });
        escenaMenu.addActor(btnPlay);
        escenaMenu.addActor(btnHelp);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(textFondo,0,0);
        //COORDENADAS ENGRANE GRANDE
        //COORDENADAS ENGRANE MEDIANO
        //COORDENADAS ENGRANE PEQUEÑO
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
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        textSmash.dispose();
        textFondo.dispose();
        batch.dispose();
    }

    public class Procesador extends InputAdapter {

        @Override                   //Donde hemos tocado   // Que hemos tocado  //
        public boolean touchDown(int screenX, int screenY, int pointer, int button){
            System.out.println("Tocado en posicion " + screenX + ", " + screenY );
            return true;
        }
    }
}

