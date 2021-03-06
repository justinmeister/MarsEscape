package com.armstrongjustin.marsescape.Screens;

import com.armstrongjustin.marsescape.MarsEscape;
import com.armstrongjustin.marsescape.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestLevel implements Screen {

    private final int PORTWIDTH = 800;
    private final int PORTHEIGHT = 416;
    private final String TILEMAP = "TestMap.tmx";
    private final String GROUND_LAYER = "ground";
    private final String SPRITE_LAYER = "spriteStartPosition";
    private final String PLAYER_START_POS = "player_start_pos";

    private MarsEscape game;
    private Player player;
    private OrthographicCamera camera;
    private Viewport viewport;

    private TmxMapLoader mapLoader;
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private MapLayer groundLayer;
    private MapObjects groundObjects;

    private MapLayer spriteLayer;
    private MapObjects spritePosObjects;


    public TestLevel(MarsEscape game) {
        this.game = game;
        setUpCamera();
        setUpTileMap();
        getLayerObjects();
        makePlayer();
    }

    private void setUpCamera() {
        camera = new OrthographicCamera(PORTWIDTH, PORTHEIGHT);
        viewport = new FitViewport(PORTWIDTH, PORTHEIGHT, camera);
        camera.position.set(PORTWIDTH/2, PORTHEIGHT / 2, 0);
        camera.update();
    }

    private void setUpTileMap() {
        mapLoader = new TmxMapLoader();
        tileMap = mapLoader.load(TILEMAP);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
    }

    private void getLayerObjects() {
        groundLayer = tileMap.getLayers().get(GROUND_LAYER);
        groundObjects = groundLayer.getObjects();

        spriteLayer = tileMap.getLayers().get(SPRITE_LAYER);
        spritePosObjects = spriteLayer.getObjects();

    }

    private void makePlayer(){
        Rectangle startPosRect = new Rectangle();

        for (MapObject object : spritePosObjects) {
            if (object.getName().equals(PLAYER_START_POS)) {
                startPosRect = ((RectangleMapObject) object).getRectangle();
            }
        }

        Vector2 startPos = new Vector2(startPosRect.getX(), startPosRect.getY());
        player = new Player(startPos);


        
    }

    public void update(float dt) {
        player.update(dt);
        updateCameraPosition(dt);
        tiledMapRenderer.setView(camera);

    }

    public void updateCameraPosition(float dt) {

        if (player.getX() > camera.position.x) {
            float newXPosition = camera.position.x+(player.xVelocity*dt);
            newXPosition = Math.round(newXPosition);
            camera.position.set(newXPosition, camera.position.y, 0);
        }
        camera.update();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.render();
        game.batch.begin();
        player.render(game.batch, delta);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        player.disposeTextures();

    }
}
