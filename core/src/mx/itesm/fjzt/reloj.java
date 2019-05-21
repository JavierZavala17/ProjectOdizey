package mx.itesm.fjzt;

import com.badlogic.gdx.audio.Sound;
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

public class reloj {

    protected World world;
    protected TiledMap map;
    protected Fixture fixture;
    protected MapObject object;

    public static int usos = 5;

    public reloj(TiledMap mapa, World mundo) {

        //Reloj
        BodyDef clock = new BodyDef();
        PolygonShape clockFin = new PolygonShape();
        FixtureDef clockfix = new FixtureDef();
        Body clockcuerpo;
        for (MapObject object : mapa.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangulofin = ((RectangleMapObject) object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            clock.type = BodyDef.BodyType.StaticBody;
            clock.position.set((rectangulofin.getX() + rectangulofin.getWidth() / 2), (rectangulofin.getY() + rectangulofin.getHeight() / 2));

            clockcuerpo = mundo.createBody(clock);

            clockFin.setAsBox(rectangulofin.getWidth() / 2, rectangulofin.getHeight() / 2);
            clockfix.shape = clockFin;
            clockfix.filter.categoryBits = Pantalla.BIT_OBJETOS;
            clockfix.filter.maskBits =  Pantalla.BIT_JUGADOR | Pantalla.BIT_SUELO | Pantalla.BIT_ZILO;

            clockcuerpo.createFixture(clockfix);
        }

    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

}
