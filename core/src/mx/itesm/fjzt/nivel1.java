package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class nivel1 extends Pantalla {

    private JuegoDemo juego;

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

    //Jugador
    private Jugador jugador;

    //Alacran

    public nivel1(JuegoDemo juego) {

        atlas = new TextureAtlas("Personajes.pack");

        this.juego = juego;

        // camara = new OrthographicCamera(Pantalla.ANCHO,Pantalla.ALTO);
        camara = new OrthographicCamera(ANCHO, ALTO);
        vista = new StretchViewport(ANCHO/PX, ALTO/PX, camara);
        interfaz = new InterfazJugador(juego.batch);

        //Tile Map Part
        mapLoader = new TmxMapLoader();
        mapa = mapLoader.load("Odizey1.tmx");
        renderer = new OrthogonalTiledMapRenderer(mapa,1/PX);
        camara.position.set(vista.getWorldWidth()/2 + 740, vista.getWorldHeight()/2,0);

        mundo = new World(new Vector2(0,-10 ), true);
        //box2dRenderer = new Box2DDebugRenderer();

        // Jugador
        jugador = new Jugador(mundo, this );

        //Poner esto en clase de cada personaje
        BodyDef cuerpoDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixture = new FixtureDef();
        Body cuerpo;

        for(MapObject object : mapa.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangulo = ((RectangleMapObject)object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            cuerpoDef.type = BodyDef.BodyType.StaticBody;
            cuerpoDef.position.set((rectangulo.getX() + rectangulo.getWidth()/2)/PX,(rectangulo.getY() + rectangulo.getHeight()/2)/PX);

            cuerpo = mundo.createBody(cuerpoDef);

            shape.setAsBox(rectangulo.getWidth()/2/PX , rectangulo.getHeight()/2/PX );
            fixture.shape = shape;
            cuerpo.createFixture(fixture);
        }

        //Salida
        BodyDef findef = new BodyDef();
        PolygonShape shapefin = new PolygonShape();
        FixtureDef fixturefin = new FixtureDef();
        Body cuerpofin;
        for(MapObject object : mapa.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangulofin = ((RectangleMapObject)object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            findef.type = BodyDef.BodyType.StaticBody;
            findef.position.set((rectangulofin.getX() + rectangulofin.getWidth()/2)/PX,(rectangulofin.getY() + rectangulofin.getHeight()/2)/PX);

            cuerpofin = mundo.createBody(findef);

            shapefin.setAsBox(rectangulofin.getWidth()/2/PX , rectangulofin.getHeight()/2/PX );
            fixturefin.shape = shapefin;
            cuerpofin.createFixture(fixturefin);
        }

        mundo.setContactListener(new checaColisiones() );

    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //juego.batch.setProjectionMatrix(camara.combined);

        renderer.render();

        //box2dRenderer.render(mundo,camara.combined);

        juego.batch.setProjectionMatrix(camara.combined);
        juego.batch.begin();
        jugador.draw(juego.batch);
        juego.batch.end();

        juego.batch.setProjectionMatrix(interfaz.stage.getCamera().combined);
        interfaz.stage.draw();

    }

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
        mapa.dispose();
        renderer.dispose();
        mundo.dispose();
        interfaz.dispose();
        box2dRenderer.dispose();

    }

    public void EventosInput(float dt){
        //Mover camara con click
        /** if(Gdx.input.isTouched()){
         camara.position.x += 1000 * dt;
         }**/
        //Salto

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            jugador.cuerpo.applyLinearImpulse(new Vector2(0,6.8f),jugador.cuerpo.getWorldCenter(), true);

        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && jugador.cuerpo.getLinearVelocity().x <= 2){
            jugador.cuerpo.applyLinearImpulse(new Vector2(0.2f,0),jugador.cuerpo.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && jugador.cuerpo.getLinearVelocity().x >= -2){
            jugador.cuerpo.applyLinearImpulse(new Vector2(0-.2f,0),jugador.cuerpo.getWorldCenter(), true);
        }

    }

    public void update(float dt){
        EventosInput(dt);

        //Calculos por segundo
        mundo.step(1/60f,6,2);

        //Movimiento jugador y camara

        jugador.update(dt);
        interfaz.update(dt);
        camara.position.x = jugador.cuerpo.getPosition().x;
        camara.update();

        //Solo render a lo que la camara ve
        renderer.setView(camara);

    }

    public TiledMap getMapa(){
        return mapa;
    }

    public World getMundo(){
        return mundo;
    }


}
