package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaSpaceInvaders implements Screen {

    private final JuegoDemo juego;

    private Viewport vista;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    //ALIENS
    private Array<Alien> arrAliens;     //chingo de aliens7
    private int cuentaPasos;
    private float dx = +0.5f;    //VELOCIDAD DE LOS ALIENS EN EJE X //+1,derecha, -1,izq
    private int dy =  -5;       //VELOCIDAD DE LOS ALIENS EN EJE Y

    //NAVE
    private Nave nave;  //Jugador

    //BALA
    private Proyectil bala;

    //Boton BACK
    private Texture textBtnBack;

    //MARCADOR
    private int puntos = 0;
    private Texto texto;

    public PantallaSpaceInvaders(JuegoDemo juego) {
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

        //MARCADOR
        texto = new Texto();

        //Crear los ALIENS
        crearAliens();

        //Crear NAVE
        crearNave();

        textBtnBack = new Texture("backbtn.png");

        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        Gdx.input.setCatchBackKey(true);
    }

    private void crearNave() {
        nave = new Nave(PantallaCargando.ANCHO/2,0.05f*PantallaCargando.ALTO);
    }

    private void crearAliens() {
        arrAliens = new Array<Alien>();
        float y = 0.75f*PantallaCargando.ALTO;
        float x = 200;
        float incX = 80;
        float incY = 70;
        for (int r=0; r<5; r++){
            for (int c=0; c<11; c++){
                Alien alien = new Alien(x,y);
                arrAliens.add(alien);
                x += incX;
            }
            y -= incY;
            x = 200;
        }
    }

    @Override
    public void render(float delta) {
        actualizarAliens();
        actualizarBala(delta);

        if (bala != null){
            probarColisiones(); //bala vs alien
        }

        Gdx.gl.glClearColor(0,0,0,1); //NEGRO
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined); //escalar

        //Dibujar
        batch.begin();

        //ALIEN
        for (Alien alien :
                arrAliens){
            alien.render(batch);
        }

        //NAVE
        nave.render(batch);

        //BALA
        if (bala != null){
            bala.render(batch);
        }

        //Dibujar tbn back
        batch.draw(textBtnBack,0,PantallaCargando.ALTO - textBtnBack.getHeight());

        //Dibujar marcador
        texto.mostrarTexto(batch,puntos+"",PantallaCargando.ANCHO/2,0.9f*PantallaCargando.ALTO);

        batch.end();
    }

    private void probarColisiones() {
        for (int i=arrAliens.size-1; i>= 0; i--) {
            Alien alien = arrAliens.get(i);
            if ((alien.getEstado() == EstadoAlien.ARRIBA || alien.getEstado() == EstadoAlien.ABAJO)) {
                if (alien.getSprite().getBoundingRectangle().overlaps(bala.getSprite().getBoundingRectangle())) {
                    //arrAliens.removeIndex(i);
                    alien.setEstado(EstadoAlien.MURIENDO);
                    bala = null;
                    puntos += 10;
                    break;
                }
            }
            if (alien.getEstado()==EstadoAlien.DESTRUIDO){
                arrAliens.removeIndex(i); //AHORA SI se elimina
            }
        }
    }

    private void actualizarBala(float delta) {
        if (bala != null){
            bala.moverY(delta);
            //Verificar que sale de la pantalla
            if (bala.getSprite().getY() > PantallaCargando.ALTO){
                bala = null;
            }
        }
    }

    private void actualizarAliens() {
        for (Alien alien:
                arrAliens){
            alien.moverX(dx);
        }
        cuentaPasos++;
        if (cuentaPasos>100){
            cuentaPasos = 0;
            dx = -dx;
            for (Alien alien :
                    arrAliens){
                alien.moverY(dy);
            }
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
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        arrAliens.clear();
    }

    private class ProcesadorEntrada implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.RIGHT){
                nave.moverX(20);
            }else  if (keycode == Input.Keys.LEFT){
                nave.moverX(-20);
            }else if (keycode == Input.Keys.SPACE){
                //Disparar (nave)
                if (bala == null){
                    // No existe, crearla
                    bala = new Proyectil(nave.getSprite().getX(), nave.getSprite().getY());

                }
            }else if (keycode==Input.Keys.BACK){
                juego.setScreen(new PantallaMenu(juego));
            }

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
            Vector3 v3 = new Vector3(screenX,screenY,0);
            camera.unproject(v3);

            //Verificar btn back
            float altoRegionBtn = PantallaCargando.ALTO - textBtnBack.getHeight();

            if (v3.x < textBtnBack.getWidth() && v3.y > altoRegionBtn){
                //Quiere regresar
                juego.setScreen(new PantallaMenu(juego));
            }
            if (v3.x < PantallaCargando.ANCHO/2){
                nave.moverX(-40);
            }else if (v3.x > PantallaCargando.ANCHO/2){
                nave.moverX(+40);
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
