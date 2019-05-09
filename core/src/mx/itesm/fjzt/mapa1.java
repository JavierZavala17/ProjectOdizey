package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class mapa1 extends Pantalla {

    //Dimensiones
    public static final int ANCHO_MAPA = 8000;
    public static final int ALTO_MAPA = 736;

    //Juego
    private final JuegoDemo juego;

    //Mapa
    private TmxMapLoader mapLoader;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //
    private Texture texturaSilo;
    private JugadorNuevo silo;

    //HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;

    //Btn Pausa : Pendiente
    private Texture textureBtnPausa;
    private Texture textureMenu;
    private Objeto btnPausa;

    //Musica
    private final AssetManager manager;
    private Music musicaFondo;
    private Sound effectoDisparo;


    //Joystick
    private Touchpad pad;

    //Pausa
    //Sprite de Cuadrado
    private Sprite spriteCuadro;
    private Texture textureCuadro;

    //AssetManager
    private AssetManager assetManager;
    private EstadoJuego estado = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;

    public mapa1(JuegoDemo juego) {
        super(juego);
        this.juego = juego;
        manager = juego.getAssetManager();
        this.preferencias = juego.getPreferences();
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
        cargarMapa();
        crearHUD();

        cargarMusicas();

        // El input es el joystick virtual y el botón
        Gdx.input.setInputProcessor(escenaHUD);

    }

    private void crearHUD() {
        // Cámara HUD
        camaraHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);

        // HUD
        Skin skin = new Skin();
        manager.load("touchpad.png",Texture.class);
        manager.load("nodo.png",Texture.class);
        manager.finishLoading();
        skin.add("padBack", manager.get("touchpad.png"));
        skin.add("padKnob", manager.get("nodo.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padBack");
        estilo.knob = skin.getDrawable("padKnob");

        pad = new Touchpad(20, estilo);
        pad.setBounds(20, 20, 150, 150);

        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                if (pad.getKnobPercentX()>0.20) {
                    silo.setEstadoMovimiento(JugadorNuevo.EstadoMovimiento.MOV_DERECHA);
                } else if (pad.getKnobPercentX()<-0.20){
                    silo.setEstadoMovimiento(JugadorNuevo.EstadoMovimiento.MOV_IZQUIERDA);
                } else {
                    silo.setEstadoMovimiento(JugadorNuevo.EstadoMovimiento.QUIETO);
                }
            }
        });

        escenaHUD = new Stage(vistaHUD);
        escenaHUD.addActor(pad);
        pad.setColor(1,1,1,.85f);

        // Salto
        manager.load("btnSaltar.png",Texture.class);
        manager.finishLoading();
        Texture texturaBtn = manager.get("btnSaltar.png");
        TextureRegionDrawable trBtn = new TextureRegionDrawable(new TextureRegion(texturaBtn));
        manager.load("btnSaltar2.png",Texture.class);
        manager.finishLoading();
        Texture texturaBtnBAjo = manager.get("btnSaltar2.png");
        TextureRegionDrawable trBtnBajo = new TextureRegionDrawable(new TextureRegion(texturaBtnBAjo));
        ImageButton btnSalto = new ImageButton(trBtn, trBtnBajo);
        btnSalto.setPosition(ANCHO-btnSalto.getWidth(), 10);
        btnSalto.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                silo.saltar();
                return true;
            }
        });
        escenaHUD.addActor(btnSalto);

        // Pausa
        manager.load("btnPausa.png",Texture.class);
        manager.finishLoading();
        Texture texturaPausa = manager.get("btnPausa.png");
        TextureRegionDrawable trBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaPausa));
        ImageButton btnPausa = new ImageButton(trBtnPausa);
        btnPausa.setPosition(ANCHO-btnPausa.getWidth(), ALTO-btnPausa.getHeight());
        btnPausa.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Se pausa el juego
                estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;
                if (estado==EstadoJuego.PAUSADO) {
                    // Activar escenaPausa y pasarle el control
                    if (escenaPausa==null) {
                        escenaPausa = new EscenaPausa(vistaHUD, batch);
                    }
                    Gdx.input.setInputProcessor(escenaPausa);
                }
                return true;
            }
        });
        escenaHUD.addActor(btnPausa);
    }

    private void crearObjetos() {
        silo = new JugadorNuevo(texturaSilo,0,64);
        silo.setEstadoMovimiento(JugadorNuevo.EstadoMovimiento.QUIETO);
    }

    private void cargarTexturas() {
        manager.load("Linea-Silo-Co.png",Texture.class);
        manager.finishLoading();
        texturaSilo = manager.get("Linea-Silo-Co.png");
    }

    private void cargarMusicas() {
        musicaFondo = manager.get("musicaLevel1.mp3");
        musicaFondo.setLooping(true);
        musicaFondo.play();

    }

    private void cargarMapa() {
        mapLoader = new TmxMapLoader();
        mapa = mapLoader.load("Mapa1.tmx");
        manager.load("musicaLevel1.mp3",Music.class);
        manager.finishLoading();
        musicaFondo = manager.get("musicaLevel1.mp3");
        musicaFondo.setLooping(true);
        musicaFondo.play();

        batch = new SpriteBatch();

        rendererMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        rendererMapa.setView(camera);
    }

    @Override
    public void render(float delta) {
        silo.actualizar(mapa);
        actualizarCamara();
        borrarPantalla();

        batch.setProjectionMatrix(camera.combined);
        rendererMapa.setView(camera);
        rendererMapa.render();

        batch.begin();
        silo.dibujar(batch);
        batch.end();

        // HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();

        if (estado==EstadoJuego.PAUSADO) {
            escenaPausa.draw();
        }



    }

    private void actualizarCamara() {
        float posX = silo.sprite.getX();
        // Si está en la parte 'media'
        if (posX>=ANCHO/2 && posX<=ANCHO_MAPA-ANCHO/2) {
            // El personaje define el centro de la cámara
            camera.position.set((int)posX, camera.position.y, 0);
        } else if (posX>ANCHO_MAPA-ANCHO/2) {    // Si está en la última mitad
            // La cámara se queda a media pantalla antes del fin del mundo  :)
            camera.position.set(ANCHO_MAPA-ANCHO/2, camera.position.y, 0);
        } else if ( posX<ANCHO/2 ) { // La primera mitad
            camera.position.set(ANCHO/2, ALTO/2,0);
        }
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        /*manager.unload("musicaLevel1.mp3");
        manager.unload("Mapa1.tmx");
        manager.unload("Linea-Silo-Co.png");*/

    }

    // La escena que se muestra cuando el juego se pausa
    // (simplificado, ver la misma escena en PantallaWhackAMole)
    private class EscenaPausa extends Stage
    {
        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

            Pixmap pixmap = new Pixmap((int) (ANCHO), (int) (ALTO), Pixmap.Format.RGBA8888);
            pixmap.setColor(1f, 1f, 1f, .5f);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture texturaRectangulo = new Texture(pixmap);
            pixmap.dispose();
            Image rectImg = new Image(texturaRectangulo);
            rectImg.setPosition(0,0);
            this.addActor(rectImg);


            manager.load("CuadroAjustes.png",Texture.class);
            manager.finishLoading();
            textureCuadro = manager.get("CuadroAjustes.png");
            Image cuadroImg = new Image(textureCuadro);
            cuadroImg.setPosition(0,0);
            this.addActor(cuadroImg);

            manager.load("btnMenu.png",Texture.class);
            manager.finishLoading();
            textureMenu = manager.get("btnMenu.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(new TextureRegion(textureMenu));
            ImageButton btnMenu = new ImageButton(trdSalir);
            btnMenu.setPosition((ANCHO/2- btnMenu.getWidth()/2), (ALTO/2)+50);
            btnMenu.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    musicaFondo.stop();
                    musicaMenus = true;
                    savePreferences();
                    juego.setScreen(new PantallaCargando(juego));

                }
            });
            this.addActor(btnMenu);

            textureBtnPausa = manager.get("btnPausa.png");
            TextureRegionDrawable trdContinuar = new TextureRegionDrawable(
                    new TextureRegion(textureBtnPausa));
            ImageButton btnPausa = new ImageButton(trdContinuar);
            btnPausa.setPosition(ANCHO-btnPausa.getWidth(), ALTO-btnPausa.getHeight());
            btnPausa.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // return to the game
                    cargarMapa();
                    Gdx.input.setInputProcessor(escenaHUD);
                    estado= EstadoJuego.JUGANDO;
                }
            });
            this.addActor(btnPausa);

        }
    }
}
