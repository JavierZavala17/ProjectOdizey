package mx.itesm.fjzt;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class ConvertidorMapa2 {


    public static void crearCuerpos(TiledMap mapa, World mundo) {
        //Poner esto en clase de cada personaje
        BodyDef cuerpoDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixture = new FixtureDef();
        Body cuerpo;

        //Salida
        for(MapObject object : mapa.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangulo = ((RectangleMapObject)object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            cuerpoDef.type = BodyDef.BodyType.StaticBody;
            cuerpoDef.position.set((rectangulo.getX() + rectangulo.getWidth()/2),(rectangulo.getY() + rectangulo.getHeight()/2));

            cuerpo = mundo.createBody(cuerpoDef);

            shape.setAsBox(rectangulo.getWidth()/2, rectangulo.getHeight()/2 );
            fixture.shape = shape;
            fixture.filter.categoryBits = Pantalla.BIT_WIN;
            cuerpo.createFixture(fixture);
        }

        //Suelo
        BodyDef findef = new BodyDef();
        PolygonShape shapefin = new PolygonShape();
        FixtureDef fixturefin = new FixtureDef();
        Body cuerpofin;
        for(MapObject object : mapa.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangulofin = ((RectangleMapObject)object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            findef.type = BodyDef.BodyType.StaticBody;
            findef.position.set((rectangulofin.getX() + rectangulofin.getWidth()/2),(rectangulofin.getY() + rectangulofin.getHeight()/2));

            cuerpofin = mundo.createBody(findef);

            shapefin.setAsBox(rectangulofin.getWidth()/2 , rectangulofin.getHeight()/2 );
            fixturefin.shape = shapefin;
            cuerpofin.createFixture(fixturefin);
        }

        //enemigo
        BodyDef clock = new BodyDef();
        PolygonShape clockFin = new PolygonShape();
        FixtureDef clockfix = new FixtureDef();
        Body clockcuerpo;
        for (MapObject object : mapa.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangulofin = ((RectangleMapObject) object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            clock.type = BodyDef.BodyType.StaticBody;
            clock.position.set((rectangulofin.getX() + rectangulofin.getWidth() / 2), (rectangulofin.getY() + rectangulofin.getHeight() / 2));

            clockcuerpo = mundo.createBody(clock);

            clockFin.setAsBox(rectangulofin.getWidth() / 2, rectangulofin.getHeight() / 2);
            clockfix.shape = clockFin;
            clockfix.filter.categoryBits = Pantalla.BIT_ENEMIGO;
            clockfix.filter.maskBits = Pantalla.BIT_BALA | Pantalla.BIT_ENEMIGO| Pantalla.BIT_JUGADOR | Pantalla.BIT_PAREDES_ENEMIGOS| Pantalla.BIT_SUELO | Pantalla.BIT_ZILO;

            clockcuerpo.createFixture(clockfix);
        }

        //Reloj
        BodyDef clock2 = new BodyDef();
        PolygonShape clockFin2 = new PolygonShape();
        FixtureDef clockfix2 = new FixtureDef();
        Body clockcuerpo2;
        for (MapObject object : mapa.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangulofin = ((RectangleMapObject) object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            clock2.type = BodyDef.BodyType.StaticBody;
            clock2.position.set((rectangulofin.getX() + rectangulofin.getWidth() / 2), (rectangulofin.getY() + rectangulofin.getHeight() / 2));

            clockcuerpo2 = mundo.createBody(clock2);

            clockFin2.setAsBox(rectangulofin.getWidth() / 2, rectangulofin.getHeight() / 2);
            clockfix2.shape = clockFin2;
            clockfix2.filter.categoryBits = Pantalla.BIT_OBJETOS;
            clockfix2.filter.maskBits =  Pantalla.BIT_JUGADOR | Pantalla.BIT_SUELO | Pantalla.BIT_ZILO;

            clockcuerpo2.createFixture(clockfix2);
        }

    }


}