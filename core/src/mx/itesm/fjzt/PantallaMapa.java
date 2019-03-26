package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class PantallaMapa extends Pantalla {
    //Mapa
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;
    private float dx = 0;

    //Personaje
    private Personaje mario;

    //Musica
    private Music musicaFondo; // audio largo, mp3
    private Sound efecto; // Efectos, wav

    //BOX2D FISICA
    private World mundo; //SIMULACION
    private Body cuerpo; //recibe la simulacion

    public PantallaMapa(JuegoDemo juego) {
    }

    @Override
    public void show() {
        cargarMapa();
        cosntruirHUD();
        mario = new Personaje(ANCHO/2,3*ALTO/4);
        cargarMusica();

        //configurarFisica
        configurarFisica();
    }

    private void configurarFisica() {
        mundo = new World(new Vector2(0,-9.81f),true);
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(mario.getX(), mario.getY());
        cuerpo = mundo.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(mario.getWidth()/2,mario.getHeight()/2);
        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        fix.density = 1;
        Fixture fixture = cuerpo.createFixture(fix);
        shape.dispose();
    }

    private void cargarMusica() {
        AssetManager manager = new AssetManager();
        manager.load("marioBros.mp3", Music.class);
        manager.finishLoading(); //síncrono
        musicaFondo = manager.get("marioBros.mp3");
        musicaFondo.play();
        musicaFondo.setLooping(true);
    }

    private void cosntruirHUD() {
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO / 2, ALTO / 2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);

        //Pad virtual
        Skin skin = new Skin();
        skin.add("fondo", new Texture("padBack.png"));
        skin.add("boton", new Texture("padKnob.png"));
        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("fondo");
        estilo.knob = skin.getDrawable("boton");
        //Crea el pad
        Touchpad pad = new Touchpad(10, estilo);
        pad.setBounds(16, 15, 256, 256);

        //Listener del Pad
        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad p = (Touchpad) actor;
                dx = p.getKnobPercentX()*10;
            }
        });

        pad.setColor(1,1,1,0.5f);

        //Agregar las escenas
        escenaHUD = new Stage(vistaHUD);
        escenaHUD.addActor(pad);

        //Hcer que reciba input
        Gdx.input.setInputProcessor(escenaHUD);
    }

    private void cargarMapa() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("mapaMario.tmx",TiledMap.class);
        manager.finishLoading(); //BLOQUEA la app para terminar la carga
        mapa = manager.get("mapaMario.tmx");
        rendererMapa = new OrthogonalTiledMapRenderer(mapa);


    }

    @Override
    public void render(float delta) {
        //FISICA
        mundo.step(delta, 6,2);
        mario.setX(cuerpo.getPosition().x);
        mario.setY(cuerpo.getPosition().y);

        //Actualizar
        //acualizarCamara(dx);
        actualizarPersonaje(dx);

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        rendererMapa.setView(camara);
        rendererMapa.render();
        batch.begin();
        mario.render(batch);
        batch.end();

        //HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
    }

    private void actualizarPersonaje(float dx) {
        mario.moverX(dx);
        acualizarCamara();
    }

    private void acualizarCamara() {
        float xCamara = mario.getX();
        if(mario.getX()<ANCHO/2){
            xCamara = ANCHO/2;
        }
        if(mario.getX()>ANCHO){
            xCamara = ANCHO/2;
        }

        camara.position.x = xCamara;
        camara.update(); //Cambio
    }

    private void acualizarCamara(float dx) {
        float xCamara = camara.position.x;
        xCamara += dx;
        camara.position.x = xCamara;
        camara.update(); //Cambio
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
}