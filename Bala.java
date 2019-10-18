package mx.itesm.demo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bala {
    private Sprite sprite;
    private float velocidadd = 300; // pixeles*s

    private Texture texturaBala;


    public Bala (Texture textura, float x, float y){
        sprite = new Sprite(textura);
        sprite.setPosition(x,y);

    }

    public Sprite getSprite(){
        return sprite;
    }


    public void mover (float delta){
        float distancia = velocidadd*delta;
        sprite.setY(sprite.getY()+ distancia);
    }


    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
