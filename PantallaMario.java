package mx.itesm.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;


class PantallaMario extends Pantalla {
    private Juego juego;
    //Mapa
    private OrthogonalTiledMapRenderer rendererMapa;
    private TiledMap mapa;

    //Audio
    private Music audioFondo;
    private Sound efecto;

    //Estados
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;

    //Pausa
    private EscenaPausa escenaPausa;

    //particula
    private ParticleEffect pe;
    private ParticleEmitter emisor;





    public PantallaMario(Juego juego) {
    this.juego = juego;
    }

    @Override
    public void show() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver())); //interpreta el achivo del mapa
        manager.load("mapa/mapaMario.tmx", TiledMap.class);
        //Cargar audios
        manager.load("audio/sms.mp3", Music.class);
        manager.load("audio/bruh.mp3", Sound.class);

        manager.finishLoading();   //Segundo plano los elementos}
        mapa=manager.get("mapa/mapaMario.tmx");
        rendererMapa = new OrthogonalTiledMapRenderer(mapa);
        //leer audios
        audioFondo= manager.get("audio/sms.mp3");
        efecto = manager.get("audio/bruh.mp3");


        audioFondo.setLooping(true);
        audioFondo.play();
        audioFondo.setVolume(0.2f);

        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        //particulas
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("fuego.p"),Gdx.files.internal(""));
        Array<ParticleEmitter> emisores = pe.getEmitters();
        emisores.get(0).setPosition(ANCHO/2, ALTO/2);
        pe.start();

    }

    @Override
    public void render(float delta) {

        pe.update(delta);

        borrarPantalla(1,0,0);
        batch.setProjectionMatrix(camara.combined);
        rendererMapa.setView(camara);

        rendererMapa.render();
        batch.begin();
        pe.draw(batch);
        batch.end();


        if (estadoJuego==EstadoJuego.PAUSADO){
            escenaPausa.draw();
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private class ProcesadorEntrada implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            efecto.play();
            //pausa
            if (estadoJuego == EstadoJuego.JUGANDO){
                estadoJuego = EstadoJuego.PAUSADO;
                audioFondo.pause();
                if (escenaPausa == null){
                    escenaPausa = new EscenaPausa(vista,batch);
                }
            }else{
                estadoJuego = EstadoJuego.JUGANDO;
                audioFondo.play();
            }

            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
    class EscenaPausa extends Stage {
        public EscenaPausa(Viewport vista, SpriteBatch batch){
            super(vista,batch);
            Pixmap pixmap = new Pixmap((int)(ANCHO*.7f), (int)(ALTO*.8f), Pixmap.Format.RGBA8888);
            pixmap.setColor(0,0,0,0.8f);
            pixmap.fillRectangle(0,0,pixmap.getWidth(),pixmap.getHeight());
            //pixmap.fillCircle(300,300,300);
            Texture texturaRect = new Texture(pixmap);

            Image imgRectangulo = new Image(texturaRect);
            imgRectangulo.setPosition(ANCHO/2 - pixmap.getWidth()/2,
                    ALTO/2 -pixmap.getHeight()/2);
            this.addActor(imgRectangulo);
        }
    }
}
