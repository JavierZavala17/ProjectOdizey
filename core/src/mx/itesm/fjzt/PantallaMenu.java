package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
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

public class PantallaMenu extends Pantalla {

    private final JuegoDemo juego;

    private Texture textFondo;

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

    //Sprite de manecilla
    private Sprite spriteManecilla;
    private Texture textureManecilla;

    private final AssetManager assetManager; // = new AssetManager();

    private boolean musica = true;

    public PantallaMenu(JuegoDemo juego) {
        this.juego = juego;
        assetManager = juego.getAssetManager();
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

        textFondo = new Texture("PantallaMenuFondo.png");

        //ENGRANE GRANDE
        textEngraneGrande = new Texture("EngraneGrandeMenu.png");
        spriteEngraneGrande = new Sprite(textEngraneGrande);

        //ENGRANE MEDIANO
        textureEngraneMediano = new Texture("EngraneMedianoMenu.png");
        spriteEngraneMediano = new Sprite(textureEngraneMediano);

        //ENGRANE PEQUEÑO
        textureEngranePequeño = new Texture("EngraneChicoMenu.png");
        spriteEngranePequeño = new Sprite(textureEngranePequeño);

        //MANECILLA
        textureManecilla = new Texture("Manecilla.png");
        spriteManecilla = new Sprite(textureManecilla);


        //Menú
        crearMenu();
        //Pasamos el control de INPUT a la escena
        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(true);
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //Botón JUGAR
        Texture textBtnPlay = new Texture("btnJugar.png");
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(textBtnPlay));

        Texture textBtnPlayP = new Texture("btnJugar2.png");
        TextureRegionDrawable trdBtnPlayP = new TextureRegionDrawable(new TextureRegion(textBtnPlayP));

        ImageButton btnPlay = new ImageButton(trdBtnPlay,trdBtnPlayP);
        btnPlay.setPosition(790,280);
        escenaMenu.addActor(btnPlay);
        //Agregar el LISTENER
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new PantallaSeleccionNivel(juego));
            }
        });

        //Botón Ajustes
        Texture textBtnAjustes = new Texture("btnAjustes.png");
        TextureRegionDrawable trdBtnAjustes = new TextureRegionDrawable(new TextureRegion(textBtnAjustes));

        Texture textBtnAjustesS = new Texture("btnAjustes2.png");
        TextureRegionDrawable trdBtnAjustedsS = new TextureRegionDrawable(new TextureRegion(textBtnAjustesS));

        ImageButton btnHelp = new ImageButton(trdBtnAjustes, trdBtnAjustedsS);
        btnHelp.setPosition(760,195);
        // CARGAR LA PANTALLA DE MAPAS
        btnHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new PantallaAjustes(juego));
            }
        });
        escenaMenu.addActor(btnHelp);

        //Botón Ayuda
        Texture textBtnAyuda = new Texture("btnAyuda.png");
        TextureRegionDrawable trdBtnAyuda = new TextureRegionDrawable(new TextureRegion(textBtnAyuda));

        Texture textBtnAyudaA = new Texture("btnAyuda2.png");
        TextureRegionDrawable trdBtnAyudaA = new TextureRegionDrawable(new TextureRegion(textBtnAyudaA));

        ImageButton btnAyuda = new ImageButton(trdBtnAyuda, trdBtnAyudaA);
        btnAyuda.setPosition(730,110);
        // CARGAR LA PANTALLA DE MAPAS
        btnAyuda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new PantallaAyuda(juego));
            }
        });
        escenaMenu.addActor(btnAyuda);

        //Botón DontEnter
        Texture textBtnDontEnter = new Texture("btnCreditos.png");
        TextureRegionDrawable trdBtnDontEnter = new TextureRegionDrawable(new TextureRegion(textBtnDontEnter));

        Texture textBtnDontEnterR = new Texture("btnCreditos2.png");
        TextureRegionDrawable trdBtnDontEnterR = new TextureRegionDrawable(new TextureRegion(textBtnDontEnterR));

        ImageButton btnDontEnter = new ImageButton(trdBtnDontEnter, trdBtnDontEnterR);
        btnDontEnter.setPosition(700,25);
        // CARGAR LA PANTALLA DE MAPAS
        btnDontEnter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Responder al evento del boton
                juego.setScreen(new PantallaMasInfo(juego));
            }
        });
        escenaMenu.addActor(btnDontEnter);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(musica == true){
            juego.iniciarMusica();
        }
        else if(musica == false){
            juego.pausarMusica();
        }


        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(textFondo,0,0);

        //COORDENADAS ENGRANE GRANDE
        spriteEngraneGrande.setX(62 - spriteEngraneGrande.getWidth()/2);
        spriteEngraneGrande.setY(455 - spriteEngraneGrande.getHeight()/2);
        spriteEngraneGrande.rotate(-0.3f);
        spriteEngraneGrande.draw(batch);

        //COORDENADAS ENGRANE MEDIANO
        spriteEngraneMediano.setX(62 - spriteEngraneMediano.getWidth()/2);
        spriteEngraneMediano.setY(455 - spriteEngraneMediano.getHeight()/2);
        spriteEngraneMediano.rotate(0.35f);
        spriteEngraneMediano.draw(batch);

        //COORDENADAS ENGRANE PEQUEÑO
        spriteEngranePequeño.setX(62 - spriteEngranePequeño.getWidth()/2);
        spriteEngranePequeño.setY(455 - spriteEngranePequeño.getHeight()/2);
        spriteEngranePequeño.rotate(-0.37f);
        spriteEngranePequeño.draw(batch);

        //MANECILLA
        spriteManecilla.setX(61.5f -spriteManecilla.getWidth()/2);
        spriteManecilla.setY(456 - spriteManecilla.getHeight()/2);
        spriteManecilla.rotate(0.65f);
        spriteManecilla.draw(batch);

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
        textFondo.dispose();
        textEngraneGrande.dispose();
        textureEngraneMediano.dispose();
        textureEngranePequeño.dispose();
        batch.dispose();
    }
}

