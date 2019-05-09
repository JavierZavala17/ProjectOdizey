package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class PantallaCargando extends Pantalla {

    private final JuegoDemo juego;

    //Imagen
    private Texture textEspera;

    //Tiempo
    private float contadorTiempo = 0;

    //Sprite de engrane grande
    private Sprite spriteEngraneGrande;
    private Texture textEngraneGrande;

    //Sprite de engrane pequeño
    private Sprite spriteEngranePequeño;
    private Texture textureEngranePequeño;

    public PantallaCargando(JuegoDemo juego) {
        super(juego);
        this.juego = juego;
    }

    @Override
    public void show() {
        // ~ Constructor
        camera = new OrthographicCamera(ANCHO, ALTO);
        camera.position.set(ANCHO/2,ALTO/2,0);
        camera.update();
        // Vista
        vista = new StretchViewport(ANCHO,ALTO,camera);
        batch = new SpriteBatch();

        //FONDO
        textEspera = new Texture("FondoLoading.png");

        //ENGRANE GRANDE
        textEngraneGrande = new Texture("engraneGrande.png");
        spriteEngraneGrande = new Sprite(textEngraneGrande);

        //ENGRANE PEQUEÑO
        textureEngranePequeño = new Texture("engranePequeño.png");
        spriteEngranePequeño = new Sprite(textureEngranePequeño);
    }

    @Override
    public void render(float delta) {
        // Dibujar (60 fps)
        Gdx.gl.glClearColor(0,0,0,1); // Color negro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Borra_todo_alv

        batch.setProjectionMatrix(camera.combined);// ESCALA

        batch.begin();
        //Dibujes
        batch.draw(textEspera,0,0);
        //COORDENADAS ENGRANDE
        spriteEngraneGrande.setX(ANCHO-720);
        spriteEngraneGrande.setY(ALTO-410);
        spriteEngraneGrande.draw(batch);
        spriteEngraneGrande.rotate(0.49f);

        //COORDENADAS ENEÑO
        spriteEngranePequeño.setX(ANCHO-900);
        spriteEngranePequeño.setY(ALTO-500);
        spriteEngranePequeño.draw(batch);
        spriteEngranePequeño.rotate(-0.7f);
        batch.end();

        //Prueba tiempo
        contadorTiempo += delta;
        if (contadorTiempo>=2){
            //Contó 2s
            musicaMenus = true;
            juego.setScreen(new PantallaMenu(juego));
        }
    }

    @Override
    public void resize(int width, int height) {
        // Cambia el tamaño de su ventana (desktop)
    }

    @Override
    public void pause() {
        // Pausa
    }

    @Override
    public void resume() {
        // Continua
    }

    @Override
    public void dispose() {
        // Liberar todos los recursos
        batch.dispose();
        textEspera.dispose();
    }
}
