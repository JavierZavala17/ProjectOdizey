package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class nivel1 extends Pantalla {

    private JuegoDemo juego;
    private nivel1 nivel;

    public static int ganar = 1;

    private TextureAtlas atlas;

    //Camara y vistas
    private OrthographicCamera camara;
    private Viewport vista;
    private InterfazJugador interfaz;

    //Mapa
    private TmxMapLoader mapLoader;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;

    //Fisicas con Box2D
    private World mundo;
    private Box2DDebugRenderer box2dRenderer;
    private creadorMundo creator;

    //Jugador
    private Jugador jugador;

    //MENU, Escenas, Independiente de la cámara(movimiento)
    private Stage escenaMenu; //Botones

    //Alacran
    private Array<Alacran> alacrans;

    public nivel1(JuegoDemo juego) {

        atlas = new TextureAtlas("Personajes.pack");

        this.juego = juego;

        // camara = new OrthographicCamera(Pantalla.ANCHO,Pantalla.ALTO);
        camara = new OrthographicCamera(ANCHO, ALTO);
        vista = new StretchViewport(ANCHO / PX, ALTO / PX, camara);
        interfaz = new InterfazJugador(juego.batch);

        //Tile Map Part
        mapLoader = new TmxMapLoader();
        mapa = mapLoader.load("Mapa1.tmx");
        renderer = new OrthogonalTiledMapRenderer(mapa, 1 / PX);
        camara.position.set(vista.getWorldWidth() / 2, vista.getWorldHeight() / 2, 0);


        mundo = new World(new Vector2(0, -10), true);
        //box2dRenderer = new Box2DDebugRenderer();

        // Jugador
        jugador = new Jugador(mundo, this);

        creator = new creadorMundo(this);
        mundo.setContactListener(new checaColisiones());

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //Botón BACK
        Texture textBtnDontEnter = new Texture("btnOtravez.png");
        TextureRegionDrawable trdBtnDontEnter = new TextureRegionDrawable(new TextureRegion(textBtnDontEnter));

        ImageButton btnDontEnter = new ImageButton(trdBtnDontEnter);
        btnDontEnter.setPosition(50, 50);
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
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //juego.batch.setProjectionMatrix(camara.combined);

        renderer.render();

        //box2dRenderer.render(mundo,camara.combined);

        juego.batch.setProjectionMatrix(camara.combined);
        juego.batch.begin();
        jugador.draw(juego.batch);
        for (Enemigo enemigo : creator.getAlacranes()) {
            enemigo.draw(juego.batch);
        }
        juego.batch.end();

        juego.batch.setProjectionMatrix(interfaz.stage.getCamera().combined);
        interfaz.stage.draw();

        if (finJuego() || ganar == 0) {
            ganar = 1;
            juego.setScreen(new PantallaLose(juego));

        }

        if (ganar == 2) {
            ganar = 1;
            juego.setScreen(new PantallaWin(juego));
        }

        teclaBack();
    }

    private void teclaBack() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            juego.setScreen(new PantallaMenu(juego));
        }
    }

    private boolean finJuego() {
        if (interfaz.isTiempoAcabo()) {
            return true;
        }
        return false;
    }

    //I WANNA DIE
    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
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

    public void EventosInput(float dt) {
        //Mover camara con click

        //Salto
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            jugador.saltar();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && jugador.cuerpo.getLinearVelocity().x <= 2) { //vel = 0.4f
            jugador.cuerpo.applyLinearImpulse(new Vector2(.5f, 0), jugador.cuerpo.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && jugador.cuerpo.getLinearVelocity().x >= -2) {
            jugador.cuerpo.applyLinearImpulse(new Vector2(0 - .5f, 0), jugador.cuerpo.getWorldCenter(), true);
        }

    }

    public void update(float dt) {
        EventosInput(dt);

        //Calculos por segundo
        mundo.step(1 / 60f, 6, 2);

        //Movimiento jugador y camara

        jugador.update(dt);

        for (Enemigo enemigo : creator.getAlacranes()) {
            enemigo.update(dt);
            if (enemigo.getX() < jugador.getX() + 1200 / Pantalla.PX) {
                enemigo.cuerpo.setActive(true);
            }
        }

        interfaz.update(dt);
        camara.position.x = jugador.cuerpo.getPosition().x + 5;
        camara.update();

        //Solo render a lo que la camara ve
        renderer.setView(camara);

    }

    public TiledMap getMapa() {
        return mapa;
    }

    public World getMundo() {
        return mundo;
    }

    private class ProcesadorEntrada implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {

            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        // Coordenadas FISICAS
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v3 = new Vector3(screenX, screenY, 0);
            camera.unproject(v3);

            if (v3.x < PantallaCargando.ANCHO / 2 && v3.x > PantallaCargando.ALTO / 2) {
                jugador.cuerpo.applyLinearImpulse(new Vector2(-.5f, 0), jugador.cuerpo.getWorldCenter(), true);
            } else if (v3.x > PantallaCargando.ANCHO / 2 && v3.x < PantallaCargando.ALTO / 2) {
                jugador.cuerpo.applyLinearImpulse(new Vector2(.5f, 0), jugador.cuerpo.getWorldCenter(), true);
            } else if (v3.x > PantallaCargando.ALTO / 2) {
                jugador.cuerpo.applyLinearImpulse(new Vector2(1f, 5), jugador.cuerpo.getWorldCenter(), true);
            }

            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {

            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }


    }
}
