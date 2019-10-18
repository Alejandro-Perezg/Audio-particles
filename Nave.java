package mx.itesm.demo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Nave {

    private Sprite sprite;
    private Texture texturaArriba;
    private Texture texturaAbajo;

    public Nave (Texture textura, float x, float y){
        sprite = new Sprite(textura);
        sprite.setPosition(x,y);

    }

    public Sprite getSprite(){
        return sprite;
    }

    public void mover (float dx){
        sprite.setX(sprite.getX()+dx);
    }


    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
