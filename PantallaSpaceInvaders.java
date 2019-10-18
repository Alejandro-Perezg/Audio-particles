package mx.itesm.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class PantallaSpaceInvaders implements Screen {
    private final Juego juego;
    private OrthographicCamera camara;
    private Viewport vista;

    private SpriteBatch batch;


    //fondo
    private Texture texturaFondo;

    //escena de menu (botones)
    private Stage escenaHUD;

    //ENEMIGOS
    public Array<Enemigo> arrEnemigos;
    private int DX = +13;
    private int DY = -40;
    private int pasos= 0;
    private float timerPaso = 0f;
    private float MAX_PASO = 0.4f;

    //NAVE
    private Nave nave;
    private Movimiento estadoNave = Movimiento.QUIETO;

    //Balas
    private Bala bala;
    private Texture texturaBala;

    public PantallaSpaceInvaders (Juego juego) {
        this.juego = juego;   //this de pantalla inicio consrtuctor
    }

    @Override
    public void show() { //mostrar en pantalla fisica. ini items, texturas.
        configurarVista();
        cargarTexturas();
        crearHUD();
        crearEnemigos();
        crearNave();

    }

    private void crearNave() {
        Texture texturaNave = new Texture("space/nave.png");
        nave = new Nave(texturaNave, juego.ANCHO/2 , 40);
    }

    private void crearEnemigos() {
        arrEnemigos = new Array<>(11*5);
        Texture texturaEnemigosArriba = new Texture("space/enemigoArriba.png");
        Texture texturaEnemigoAbajo = new Texture("space/enemigoAbajo.png");
        for( int renglon =0; renglon<5; renglon++) {
            for (int columna = 0; columna < 11; columna++){
            Enemigo enemigo = new Enemigo(texturaEnemigosArriba,texturaEnemigoAbajo,  40 +columna *100,  350 + renglon*60);
            arrEnemigos.add(enemigo);
            }
        }

    }

    private void crearHUD() {
        escenaHUD = new Stage(vista);
        TextureRegionDrawable trdBack = new TextureRegionDrawable(new TextureRegion(new Texture("back.png")));
        TextureRegionDrawable trdBackPressed = new TextureRegionDrawable(new TextureRegion(new Texture("backPressed.png")));


        final ImageButton btnBack = new ImageButton(trdBack,trdBackPressed);
        btnBack.setPosition(0, Juego.ALTO - btnBack.getHeight());

        //Evento de boton.
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //INSTRUCCIONE
                juego.setScreen(new PantallaMenu(juego));
            }
        }
        );


        escenaHUD.addActor(btnBack);

        //botones para mover
        TextureRegionDrawable trdDerecha = new TextureRegionDrawable(new TextureRegion(new Texture("space/flechaDerecha.png")));
        TextureRegionDrawable trdIzquierda  = new TextureRegionDrawable(new TextureRegion(new Texture("space/flechaIzquierda.png")));
        ImageButton  btnDerecha = new ImageButton(trdDerecha);
        ImageButton  btnIzquierda = new ImageButton(trdIzquierda);
        btnDerecha.setPosition(150 + btnDerecha.getWidth() ,0);
        btnIzquierda.setPosition( btnDerecha.getWidth(),0);

        //Listeners
        btnDerecha.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                estadoNave = Movimiento.DERECHA;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                estadoNave = Movimiento.QUIETO;
            }


        });

        btnIzquierda.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                estadoNave = Movimiento.IZQUIERDA;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                estadoNave = Movimiento.QUIETO;
            }


        });



        escenaHUD.addActor(btnDerecha);
        escenaHUD.addActor(btnIzquierda);

        //boton para disparar
        TextureRegionDrawable trdDisparar = new TextureRegionDrawable(new TextureRegion(new Texture("btnDisparar.png")));
        TextureRegionDrawable trdDispararPressed  = new TextureRegionDrawable(new TextureRegion(new Texture("btnDispararPressed.png")));
        ImageButton  btnDisparar = new ImageButton(trdDisparar,trdDispararPressed);
        btnDisparar.setPosition(Juego.ANCHO - btnDisparar.getWidth() ,0);


        //Listeners dispatrar
        btnDisparar.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(bala==null) {
                    bala = new Bala(texturaBala, nave.getSprite().getX() + nave.getSprite().getWidth() / 2, nave.getSprite().getY() + nave.getSprite().getHeight());
                }
                return true;
            }

        });
        escenaHUD.addActor(btnDisparar);
        Gdx.input.setInputProcessor(escenaHUD);

    }

    private void cargarTexturas() {
        texturaFondo = new Texture( "fondoSpace.jpg");
        texturaBala = new Texture ("Space/bala.png");
    }

    private void configurarVista() {
        camara = new OrthographicCamera();
        camara.position.set(Juego.ANCHO/2,Juego.ALTO/2,0);
        camara.update();

        vista = new StretchViewport(Juego.ANCHO, Juego.ALTO, camara);

        batch = new SpriteBatch(); //administra los trazos.
    }


    @Override
    public void render(float delta) {
        actualizar(delta);
        borrarPantalla();

        //batch escalaTodo de acuerdo a la vista y la camara
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo, 0, 0);

        //Dibujar enemigos
        for(Enemigo enemigo:arrEnemigos){
            enemigo.render(batch);
        }
        //Dibujar Nave
        nave.render(batch);

        //Dibujar Bala
        if (bala != null) {
            bala.render(batch);
        }

        batch.end();
        escenaHUD.draw();
    }

    private void actualizar(float delta) {
        actualizarEnemigos(delta);
        actualizarNave();
        actualizarBala(delta);
        verificarChoques();
        verificarPierdeJugador();

    }

    private void verificarPierdeJugador() {
        for(Enemigo enemigo: arrEnemigos){
            if (enemigo.getSprite().getY()<= nave.getSprite().getY() + nave.getSprite().getHeight()){
                //Perdió OMAIWA MO SHINEI RU
                //congelar enemigos y desaparecer la nave
                nave.getSprite().setPosition(0,Juego.ALTO*2); //afuera de la pantalla

                break;
            }
        }
    }

    private void verificarChoques() {
        if(bala == null){
            return;
        }
        for(int i = arrEnemigos.size-1;i>=0; i-- ){
            Rectangle rectanguloBala = bala.getSprite().getBoundingRectangle();
            Rectangle rectanguloEnemigo = arrEnemigos.get(i).getSprite().getBoundingRectangle();
            if(rectanguloBala.overlaps(rectanguloEnemigo)){
                arrEnemigos.removeIndex(i);  //SHINEEEEEEE
                bala =null;
                break;
            }
        }
    }

    private void actualizarBala(float delta) {
        if (bala != null) {
            bala.mover(delta);
            if (bala.getSprite().getY()>= Juego.ALTO){ //SHINEEEEEEE
                bala = null;
            }
        }

    }

    private void actualizarNave() {
        switch(estadoNave){
            case DERECHA:
                nave.mover(10);
                break;
            case IZQUIERDA:
                nave.mover(-10);
                break;
        }
    }

    private void actualizarEnemigos(float delta) {
        if(nave.getSprite().getY() == Juego.ALTO*2){
            return;
        }
        timerPaso += delta;

        if(timerPaso>MAX_PASO) {
            timerPaso = 0;
            for (Enemigo enemigo : arrEnemigos) {
                enemigo.mover(DX, 0);
                enemigo.cambiarImagen();
            }
            pasos++;
            if (pasos >= 15) {
                pasos = 0;
                DX = -DX;
                for (Enemigo enemigo : arrEnemigos) {
                    enemigo.mover(0, DY);

                }
            }
        }
    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texturaFondo.dispose(); //liberar
    }

    private enum Movimiento {
        QUIETO,
        DERECHA,
        IZQUIERDA
    }
}
