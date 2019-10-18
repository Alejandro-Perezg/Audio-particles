package mx.itesm.demo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemigo {
    private Sprite sprite;
    private Texture texturaArriba;
    private Texture texturaAbajo;

    public Enemigo (Texture textura, float x, float y){
        sprite = new Sprite(textura);
        sprite.setPosition(x,y);

    }
    public Enemigo( Texture texturaArriba,  Texture texturaAbajo, float x,float y ){
        this.texturaArriba = texturaArriba;
        this.texturaAbajo = texturaAbajo;
        sprite = new Sprite(this.texturaArriba);
        sprite.setPosition(x,y);

    }

    public Sprite getSprite() {
        return sprite;
    }

    public void mover (float dx, float dy){
        sprite.setPosition(sprite.getX()+dx, sprite.getY()+dy);
    }

    public void cambiarImagen() {
        if(sprite.getTexture() == texturaArriba){
            sprite.setTexture(texturaAbajo);
        }else{
            sprite.setTexture(texturaArriba);
        }
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
