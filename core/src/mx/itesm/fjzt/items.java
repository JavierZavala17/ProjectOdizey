package mx.itesm.fjzt;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class items {

    private World mundo;
    private TiledMap mapa;
    private TiledMapTile tile;

    private Body cuerpo;
    private Rectangle limites;
    private Fixture fix;
    private nivel1 nivel;
    private MapObject objeto;

    public items(nivel1 nivel, MapObject objecto) {
        this.objeto = objecto;
        this.nivel = nivel;
        this.mundo = nivel.getMundo();
        this.mapa = nivel.getMapa();
        this.limites = ((RectangleMapObject) objecto).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((limites.getX() + limites.getWidth()/2)/ Pantalla.PX, (limites.getY() + limites.getHeight()/2)/ Pantalla.PX);

        cuerpo = mundo.createBody(bdef);

        shape.setAsBox((limites.getWidth()/2 )/ Pantalla.PX , (limites.getHeight() /2)/ Pantalla.PX);
        fdef.shape = shape;
        fix = cuerpo.createFixture(fdef);
    }

    public abstract void hit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fix.setFilterData(filter);
    }

        public TiledMapTileLayer.Cell getCell(){
            TiledMapTileLayer layer = (TiledMapTileLayer) mapa.getLayers().get(7); //checar cel
            return layer.getCell((int)(cuerpo.getPosition().x*Pantalla.PX/16),
                    (int)(cuerpo.getPosition().y*Pantalla.PX/16));  // 16 = tile size
        }
    }



