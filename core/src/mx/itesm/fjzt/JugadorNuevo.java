package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static mx.itesm.fjzt.Pantalla.PX;

public class JugadorNuevo extends Objeto{

    protected EstadoSalto estadoSalto = EstadoSalto.EN_PISO;
    protected EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;
    protected int vida;


    //Fisica
    public World mundo;
    public Body cuerpo;

    private Animation<TextureRegion> spriteAnimado;         // Animación caminando

    private final float VELOCIDAD_X = 2;      // Velocidad horizontal

    private float timerAnimacion;          // Tiempo para cambiar frames de la animación

    // Salto
    private float alturaSalto;  // altura actual, inicia en cero
    private float yOriginal;


    // Recibe una imagen con varios frames (ver marioSprite.png)
    public JugadorNuevo(Texture textura,int vida, float x, float y) {
        width = 64;
        height = 96;
        this.vida = vida;

        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(104,128);
        spriteAnimado = new Animation(0.1f,  texturaPersonaje[0][0], texturaPersonaje[0][1],texturaPersonaje[0][2],texturaPersonaje[0][3],texturaPersonaje[0][4],texturaPersonaje[0][5],texturaPersonaje[0][6],texturaPersonaje[0][7],texturaPersonaje[0][8],texturaPersonaje[0][9],texturaPersonaje[0][10] ,texturaPersonaje[0][11]);
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;
        sprite = new Sprite(texturaPersonaje[0][2]);    // QUIETO
        sprite.setPosition(x,y);// Posición inicial
        rectangle.set(x-1,y-1,width+2,height+2);

        // Salto
        alturaSalto = 0;

    }

    // Dibuja el personaje
    public void dibujar(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento

        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                TextureRegion region = spriteAnimado.getKeyFrame(timerAnimacion);
                if (estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                    }
                } else {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                    }
                }
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
            case QUIETO:
            case INICIANDO:
                sprite.draw(batch); // Dibuja el sprite estático
                break;
        }
    }

    // Actualiza el sprite, de acuerdo al estadoMovimiento y estadoSalto
    public void actualizar(TiledMap mapa) {
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                moverHorizontal(mapa);
                break;
        }
        switch (estadoSalto) {
            case SUBIENDO:
            case BAJANDO:
                moverVertical(mapa);
                break;
        }
    }

    // Realiza el salto
    private void moverVertical(TiledMap mapa) {
        float delta = Gdx.graphics.getDeltaTime()*200;

        int x = (int) ((sprite.getX() + 32) / 32);   // Convierte coordenadas del mundo en coordenadas del mapa
        int y = (int) (sprite.getY() / 32);
        TiledMapTileLayer plataforma = (TiledMapTileLayer) mapa.getLayers().get(2);
        TiledMapTileLayer.Cell celdaDerecha = plataforma.getCell(x, y);
        TiledMapTileLayer.Cell celdaIzquierda = plataforma.getCell(x,y);

        switch (estadoSalto) {
            case SUBIENDO:
                sprite.setY(sprite.getY()+delta);
                alturaSalto += delta;
                if (alturaSalto>=1.5*sprite.getHeight() || celdaDerecha != null || celdaIzquierda != null) {
                    estadoSalto = EstadoSalto.BAJANDO;
                }
                break;
            case BAJANDO:
                sprite.setY(sprite.getY()-delta);
                alturaSalto -= delta;
                if (alturaSalto<=0 ) {
                    estadoSalto = EstadoSalto.EN_PISO;
                    alturaSalto = 0;
                    sprite.setY(yOriginal);
                }
                break;
        }
    }


    // Mueve el personaje a la derecha/izquierda, prueba choques con paredes
    private void moverHorizontal(TiledMap mapa) {

    }

    // Revisa si toca una moneda

    /**
    public boolean recolectarReloj(TiledMap mapa) {
        // Revisar si toca una moneda (pies)
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get("relojes");
        int x = (int)(sprite.getX()/32);
        int y = (int)((sprite.getY())/32);
        TiledMapTileLayer.Cell celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "reloj".equals(tipo) ) {
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                InterfazJugador.tiempoMundo += 10;
                return true;
            }
        }
        return false;
    }
     **/

    // Accesor de estadoMovimiento
    public EstadoSalto getEstadoSalto(){
        return estadoSalto;
    }

    // Modificador de estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    // Inicia el salto
    public void saltar() {

        if (estadoSalto!=EstadoSalto.SUBIENDO && estadoSalto!=EstadoSalto.BAJANDO) {
            // inicia
            estadoSalto = EstadoSalto.SUBIENDO;
            yOriginal = sprite.getY();
            alturaSalto = 0;
        }
    }

    public void setVida(int vida){
        this.vida = vida;
    }

    public int getVida() {
        return vida;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() { return sprite.getY();}

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public void setX(float x) {
        sprite.setX(x);
    }

    public void setY(float y) {
        sprite.setY(y);
    }

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA,
    }

    public enum EstadoSalto {
        SUBIENDO,
        BAJANDO,
        EN_PISO,
        SALTANDO    // General, puede estar subiendo o bajando
    }


}