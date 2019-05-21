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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
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

public class mapa3 extends Pantalla {

    //Ganar o Perder
    public static int ganar = 1;

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
    private Texture textBarra;

    //Btn Pausa : Pendiente
    private Texture textureBtnPausa;
    private Texture textureMenu;
    private Objeto btnPausa;

    //Musica
    private Music musicaFondo;
    private Sound effectoDisparo;


    //Joystick
    private Touchpad pad;

    //Pausa
    //Sprite de Cuadrado
    private Sprite spriteCuadro;
    private Texture textureCuadro;
    private Texture textureFondo;

    //AssetManager
    private AssetManager manager;
    private EstadoJuego estado = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;

    //BOX2D Física
    private World mundo; //Simulación
    private Body cuerpo; //recibe la simulación
    private Box2DDebugRenderer debug;

    //Saltar
    private boolean saltar = false;

    //Interfaz
    private InterfazJugador interfaz;

    //Marcadores
    private int tiempo = 0;

    private Texto texto;

    //Vida
    private Texture vidaCompleta;
    private Texture vidaMenosUno;
    private Texture vidaMenosDos;

    //Reloj


    public mapa3(JuegoDemo juego) {
        super(juego);
        this.juego = juego;
        manager = juego.getAssetManager();
        interfaz = new InterfazJugador(juego.batch);

        mundo = new World(new Vector2(0,-30f),true);
        mundo.setContactListener(new checaColisiones3() );


    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
        cargarMapa();
        crearHUD();
        configurarFisica();



        cargarMusicas();

        // El input es el joystick virtual y el botón
        Gdx.input.setInputProcessor(escenaHUD);



        texto = new Texto();

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
                    cuerpo.applyLinearImpulse(new Vector2(180000,0),cuerpo.getWorldCenter(), true);

                } else if (pad.getKnobPercentX()<-0.20){
                    silo.setEstadoMovimiento(JugadorNuevo.EstadoMovimiento.MOV_IZQUIERDA);
                    cuerpo.applyLinearImpulse(new Vector2(-180000,0),cuerpo.getWorldCenter(), true);

                } else {
                    silo.setEstadoMovimiento(JugadorNuevo.EstadoMovimiento.QUIETO);
                    cuerpo.setLinearVelocity(new Vector2(0,0));
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

                if(silo.getEstadoSalto()!= JugadorNuevo.EstadoSalto.SALTANDO){
                    cuerpo.applyLinearImpulse(new Vector2(0,1800000),cuerpo.getWorldCenter(), true);
                    silo.estadoSalto = JugadorNuevo.EstadoSalto.SALTANDO;
                }
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
        btnPausa.setPosition(ANCHO-btnPausa.getWidth()-10, ALTO-btnPausa.getHeight()-10);
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
        silo = new JugadorNuevo(texturaSilo,32,64);
        silo.setEstadoMovimiento(JugadorNuevo.EstadoMovimiento.QUIETO);
    }

    private void cargarTexturas() {
        manager.load("Linea-Silo-Co.png",Texture.class);
        manager.load("interfaz.png", Texture.class);
        manager.load("vidacompleta.png", Texture.class);
        manager.load("vidamenosuno.png", Texture.class);
        manager.load("vidamenosdos.png", Texture.class);
        manager.finishLoading();

        texturaSilo = manager.get("Linea-Silo-Co.png");

        textBarra = manager.get("interfaz.png");

        vidaCompleta = manager.get("vidacompleta.png");
        vidaMenosUno = manager.get("vidamenosuno.png");
        vidaMenosDos = manager.get("vidamenosdos.png");
    }

    private void cargarMusicas() {
        musicaFondo = manager.get("musicaLevel1.mp3");

        musicaFondo.setLooping(true);
        musicaFondo.play();

    }

    private void cargarMapa() {
        mapLoader = new TmxMapLoader();
        mapa = mapLoader.load("mapa3.tmx");
        manager.load("musicaLevel1.mp3",Music.class);
        manager.finishLoading();
        musicaFondo = manager.get("musicaLevel1.mp3");
        musicaFondo.setLooping(true);
        musicaFondo.play();

        batch = new SpriteBatch();

        rendererMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        rendererMapa.setView(camera);

    }

    private void configurarFisica() {
        Box2D.init();

        BodyDef def = new BodyDef();
        def.position.set(20,128); //Posicion del sprite
        def.type = BodyDef.BodyType.DynamicBody;
        cuerpo = mundo.createBody(def);

        //Crea el box de personaje
        FixtureDef fix = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(silo.getWidth()/4,silo.getHeight()/2-1);  //
        fix.filter.categoryBits = Pantalla.BIT_JUGADOR;
        fix.filter.maskBits =  Pantalla.BIT_JUGADOR | Pantalla.BIT_SUELO | Pantalla.BIT_ZILO | Pantalla.BIT_ENEMIGO | Pantalla.BIT_OBJETOS | Pantalla.BIT_WIN;

        fix.shape = shape;
        cuerpo.createFixture(fix);

        //Agregar los bloques solidos (se configurarion en el mapa)
        ConvertidorMapa3.crearCuerpos(mapa,mundo);
        debug = new Box2DDebugRenderer(); //Esto solo se usa en desarrollo, muestra las cajas de colision


    }
    @Override
    public void render(float delta) {

        if(estado==EstadoJuego.JUGANDO) {
            //Parte de la fisica, quitar si configurarF Desactivadp
            mundo.step(delta, 6, 2);
            silo.setX(cuerpo.getPosition().x- (silo.getWidth()/2));
            silo.setY(cuerpo.getPosition().y-silo.getHeight()/2);

            interfaz.update(delta);

            silo.actualizar(mapa);
            //silo.recolectarReloj(mapa);
            actualizarCamara();
            borrarPantalla();

            batch.setProjectionMatrix(camera.combined);
            rendererMapa.setView(camera);
            rendererMapa.render();



            batch.begin();
            silo.dibujar(batch);
            batch.draw(textBarra,camera.position.x-ANCHO/2,0);
            batch.draw(vidaCompleta,camera.position.x-ANCHO/2 + 20,ALTO-vidaCompleta.getHeight());
            texto.mostrarTexto(batch,"[ Nivel:3 ]",camera.position.x-ANCHO/2 + 330,705);
            texto.mostrarTexto(batch,"[ Tiempo:  "+ interfaz.tiempoMundo()+" ]",camera.position.x-ANCHO/2 + 1030,705);


            batch.end();

            // HUD
            batch.setProjectionMatrix(camaraHUD.combined);
            /*juego.batch.setProjectionMatrix(interfaz.stage.getCamera().combined);
            interfaz.stage.draw();*/
            escenaHUD.draw();

            if(cuerpo.getLinearVelocity().y == 0){
                silo.estadoSalto = JugadorNuevo.EstadoSalto.EN_PISO;
            }
        }



        if (estado==EstadoJuego.PAUSADO) {
            escenaPausa.draw();
        }
        //Si desactivado el debug en configurarF, quitar
        debug.render(mundo,camera.combined);

        if(finJuego() || ganar <= 0){
            ganar = 1;
            juego.setScreen(new PantallaLose(juego));

        }

        if(ganar >=2){
            ganar = 1;
            juego.setScreen(new PantallaWin(juego));
        }
    }

    private boolean finJuego() {
        if(interfaz.isTiempoAcabo()){
            return true;
        }
        return false;
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



            manager.load("PantallaAjustesFondo.png",Texture.class);
            manager.finishLoading();
            textureFondo = manager.get("PantallaAjustesFondo.png");
            Image fondoImg = new Image(textureFondo);
            fondoImg.setPosition(0,0);
            this.addActor(fondoImg);

            manager.load("CuadroPausa.png",Texture.class);
            manager.finishLoading();
            textureCuadro = manager.get("CuadroPausa.png");
            Image cuadroImg = new Image(textureCuadro);
            cuadroImg.setPosition(0,0);
            this.addActor(cuadroImg);

            manager.load("btnMenu.png",Texture.class);
            manager.finishLoading();
            textureMenu = manager.get("btnMenu.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(new TextureRegion(textureMenu));
            ImageButton btnMenu = new ImageButton(trdSalir);
            btnMenu.setPosition((ANCHO/2- btnMenu.getWidth()/2), (ALTO/2)-150);
            btnMenu.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    musicaFondo.stop();

                    juego.setScreen(new PantallaMenu(juego));

                }
            });
            this.addActor(btnMenu);

            manager.load("btnReiniciar.png",Texture.class);
            manager.finishLoading();
            textureMenu = manager.get("btnReiniciar.png");
            TextureRegionDrawable trdReiniciar = new TextureRegionDrawable(new TextureRegion(textureMenu));
            ImageButton btnReiniciar = new ImageButton(trdReiniciar);
            btnReiniciar.setPosition((ANCHO/2- btnReiniciar.getWidth()/2), (ALTO/2)-50);
            btnReiniciar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    musicaFondo.stop();

                    juego.setScreen(new mapa3(juego));

                }
            });
            this.addActor(btnReiniciar);


            manager.load("btnContinuar.png",Texture.class);
            manager.finishLoading();
            textureBtnPausa = manager.get("btnContinuar.png");
            TextureRegionDrawable trdContinuar = new TextureRegionDrawable(new TextureRegion(textureBtnPausa));
            ImageButton btnPausa = new ImageButton(trdContinuar);
            btnPausa.setPosition(ANCHO/2-btnPausa.getWidth()/2, (ALTO/2)+50);
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