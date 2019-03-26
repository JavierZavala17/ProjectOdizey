package mx.itesm.fjzt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Alien {

    private static final float TIEMPO_BASE = 0.5f;
    private Texture textAlienArriba;
    private Sprite spriteArriba;
    private Sprite spriteAbajo;
    //Estado
    private EstadoAlien estado;
    private float timer = 0; //Indica cambio de estado

    public Alien(float x,float y){
        textAlienArriba = new Texture("enemigoArriba.png");
        spriteArriba = new Sprite(textAlienArriba);
        spriteArriba.setPosition(x,y);
        //Sprite Abajo
        textAlienArriba = new Texture("enemigoAbajo.png");
        spriteAbajo = new Sprite(textAlienArriba);
        spriteAbajo.setPosition(x,y);
        //Estado
        estado = EstadoAlien.ARRIBA;
    }

    public void render(SpriteBatch batch) {
        switch (estado){
            case ARRIBA:
                spriteArriba.draw(batch);
                break;
            case ABAJO:
                spriteAbajo.draw(batch);
                break;
            case MURIENDO:
                spriteArriba.setColor(1,0,0,1);
                spriteArriba.draw(batch);
                break;
        }
        // ESTADO
        timer += Gdx.graphics.getDeltaTime();

        if (timer>=TIEMPO_BASE){
            timer = 0;
            switch (estado){
                case ARRIBA:
                    estado = EstadoAlien.ABAJO;
                    break;
                case ABAJO:
                    estado = EstadoAlien.ARRIBA;
                    break;
                case MURIENDO:
                    estado  =EstadoAlien.DESTRUIDO;
                    break;
            }
        }
    }


    public void moverX(float dx) {
        spriteArriba.setX(spriteArriba.getX() + dx);
        spriteAbajo.setX(spriteAbajo.getX() + dx);
    }

    public void moverY(int dy) {
        spriteArriba.setY(spriteArriba.getY() + dy);
        spriteAbajo.setY(spriteAbajo.getY() + dy);
    }

    public Sprite getSprite(){
        return spriteArriba;
    }

    public EstadoAlien getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlien estado) {
        this.estado = estado;
    }
}
