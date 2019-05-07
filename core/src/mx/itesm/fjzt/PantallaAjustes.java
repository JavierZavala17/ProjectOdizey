package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class PantallaAjustes extends Pantalla {

    private final JuegoDemo juego;

    private Texture textFondo;

    //Sprite de engrane grande
    private Sprite spriteEngraneGrande;
    private Texture textEngraneGrande;

    //Sprite de Cuadrado
    private Sprite spriteCuadro;
    private Texture textureCuadro;

    //Sprite de manecilla
    private Sprite spriteManecilla;
    private Texture textureManecilla;

    //MENU, Escenas, Independiente de la cámara(movimiento)
    private Stage escenaMenu; //Botones

    private boolean musica = true;

    public PantallaAjustes (JuegoDemo juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        // ~ Constructor
        camera = new OrthographicCamera(ANCHO, ALTO);
        camera.position.set(ANCHO/2, ALTO /2,0);
        camera.update();
        // Vista
        vista = new StretchViewport(ANCHO, ALTO,camera);
        batch = new SpriteBatch();

        textFondo = new Texture("PantallaAjustesFondo.png");

        textureCuadro = new Texture("CuadroAjustes.png");
        spriteCuadro = new Sprite(textureCuadro);

        //ENGRANE GRANDE
        textEngraneGrande = new Texture("EngraneGrandeMenu.png");
        spriteEngraneGrande = new Sprite(textEngraneGrande);

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
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaMenu.addActor(btnDontEnter);

        //Boton VOLUMEN
        Texture textBntVolume = new Texture("btnVolumenON.png");
        TextureRegionDrawable trdBtnVolume = new TextureRegionDrawable(new TextureRegion(textBntVolume));

        Texture textBntVolumeR = new Texture("btnVolumenOFF.png");
        TextureRegionDrawable trdBtnVolumeR = new TextureRegionDrawable(new TextureRegion(textBntVolumeR));

        ImageButton btnVolume = new ImageButton(trdBtnVolume, trdBtnVolumeR);
        btnVolume.setPosition(642 - btnVolume.getWidth()/2,339 - btnVolume.getHeight()/2);
        // CARGAR LA PANTALLA DE MAPAS
        btnVolume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (musica = true){
                    musica = false;
                    juego.pausarMusica();
                }

                if (musica = false){
                    musica = true;
                    juego.musicaFondo.play();
                }

            }
        });
        escenaMenu.addActor(btnVolume);
    }

    @Override
    public void render(float delta) {
        // Dibujar (60 fps)
        Gdx.gl.glClearColor(0,0,0,1); // Color negro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Borra_todo_alv

        batch.setProjectionMatrix(camera.combined);// ESCALA

        batch.begin();

        batch.draw(textFondo,0,0);

        //COORDENADAS ENGRANE GRANDE
        spriteEngraneGrande.setX(642 - spriteEngraneGrande.getWidth()/2);
        spriteEngraneGrande.setY(339 - spriteEngraneGrande.getHeight()/2);
        spriteEngraneGrande.rotate(-0.3f);
        spriteEngraneGrande.draw(batch);

        //MANECILLA
        spriteManecilla.setX(642 -spriteManecilla.getWidth()/2);
        spriteManecilla.setY(339 - spriteManecilla.getHeight()/2);
        spriteManecilla.rotate(0.65f);
        spriteManecilla.draw(batch);

        //COORDENADAS CUADRO
        spriteCuadro.setX(0);
        spriteCuadro.setY(0);
        spriteCuadro.draw(batch);

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
    }
}
