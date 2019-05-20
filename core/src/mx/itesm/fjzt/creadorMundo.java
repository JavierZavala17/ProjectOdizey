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
import com.badlogic.gdx.utils.Array;

public class creadorMundo {

    private Array<Alacran> alacranes;

    public creadorMundo(nivel1 nivel){
        World mundo = nivel.getMundo();
        TiledMap mapa = nivel.getMapa();

        //Poner esto en clase de cada personaje
        BodyDef cuerpoDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixture = new FixtureDef();
        Body cuerpo;

        //Salida
        for(MapObject object : mapa.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangulo = ((RectangleMapObject)object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            cuerpoDef.type = BodyDef.BodyType.StaticBody;
            cuerpoDef.position.set((rectangulo.getX() + rectangulo.getWidth()/2)/Pantalla.PX,(rectangulo.getY() + rectangulo.getHeight()/2)/Pantalla.PX);

            cuerpo = mundo.createBody(cuerpoDef);

            shape.setAsBox(rectangulo.getWidth()/2/Pantalla.PX , rectangulo.getHeight()/2/Pantalla.PX );
            fixture.shape = shape;
            fixture.filter.categoryBits = Pantalla.BIT_WIN;
            cuerpo.createFixture(fixture);
        }

        //Suelo
        BodyDef findef = new BodyDef();
        PolygonShape shapefin = new PolygonShape();
        FixtureDef fixturefin = new FixtureDef();
        Body cuerpofin;
        for(MapObject object : mapa.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangulofin = ((RectangleMapObject)object).getRectangle();

            //3 tipos de body (Dynamic : se mueve; Static: Quietos; Kinematic: Afectados por ciertas fuerzas )
            findef.type = BodyDef.BodyType.StaticBody;
            findef.position.set((rectangulofin.getX() + rectangulofin.getWidth()/2)/Pantalla.PX,(rectangulofin.getY() + rectangulofin.getHeight()/2)/Pantalla.PX);

            cuerpofin = mundo.createBody(findef);

            shapefin.setAsBox(rectangulofin.getWidth()/2/Pantalla.PX , rectangulofin.getHeight()/2/Pantalla.PX );
            fixturefin.shape = shapefin;
            cuerpofin.createFixture(fixturefin);
        }


        //Crear alacranes

        alacranes = new Array<Alacran>();
        for(MapObject object : mapa.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            alacranes.add(new Alacran( nivel , rect.x/Pantalla.PX, rect.y/Pantalla.PX));
        }
    }

    public Array<Alacran> getAlacranes(){
        return alacranes;
    }


}
