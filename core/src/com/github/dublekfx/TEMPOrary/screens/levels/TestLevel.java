package com.github.dublekfx.TEMPOrary.screens.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.dublekfx.TEMPOrary.TEMPOrary;
import com.github.dublekfx.TEMPOrary.entities.friendly.Dragon;
import com.github.dublekfx.TEMPOrary.entities.friendly.Mezzo;
import com.github.dublekfx.TEMPOrary.entities.friendly.Player;
import com.github.dublekfx.TEMPOrary.utilities.Options;

public class TestLevel implements Screen {
	
	public static TestLevel instance;
	
	final TEMPOrary game;
	private TiledMap map;
	private TiledMapTileLayer layer;
	private Stage stage;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Player player;
	
	private boolean debugCooldown = false;
	
	public static final float GRAVITY = .3f;

	public TestLevel(final TEMPOrary game) {
		instance = this;
		this.game = game;
		
		TEMPOrary.INPUT.addKeyMapping("LEFT", Keys.LEFT);
		TEMPOrary.INPUT.addKeyMapping("RIGHT", Keys.RIGHT);
		TEMPOrary.INPUT.addKeyMapping("DOWN", Keys.DOWN);
		TEMPOrary.INPUT.addKeyMapping("SPACE", Keys.SPACE);
		TEMPOrary.INPUT.addKeyMapping("F3", Keys.F3);
		TEMPOrary.INPUT.addKeyMapping("ESCAPE", Keys.ESCAPE);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);
//		camera.translate((800 - 320) / 2, (600 - 240) / 2);
		viewport = new FitViewport(320, 240, camera);
		stage = new Stage(viewport);
//		viewport.translate((800 - 320) / 2, (600 - 240) / 2);
		
		map = new TmxMapLoader().load("maps/DragonStory.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		Texture playerSprite = new Texture(Gdx.files.internal("sprites/playersprites.gif"));
//		Texture playerSprite = new Texture(Gdx.files.internal("sprites/Mezzov1.png"));
		player = new Dragon(playerSprite, this);
		stage.addActor(player);
		layer = (TiledMapTileLayer) map.getLayers().get(0);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		renderer.setView(camera);
		renderer.render();
		stage.draw();
		
	}

	@SuppressWarnings("static-access")
	public void update(float delta) {
		camera.update();
		
		if(TEMPOrary.INPUT.isKeyPressed("LEFT"))  player.setLeft(true);
		if(TEMPOrary.INPUT.isKeyPressed("RIGHT")) player.setRight(true);
		if(TEMPOrary.INPUT.isKeyPressed("DOWN")) player.setDown(true);
		if(TEMPOrary.INPUT.isKeyPressed("SPACE")) player.setJumping(true);
		if(TEMPOrary.INPUT.isKeyPressed("F3") && !debugCooldown) {
			Options.DEBUG = !Options.DEBUG;
			if(Options.DEBUG) {
				Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
			}
			else {
				Gdx.app.setLogLevel(Gdx.app.LOG_INFO);
			}
			debugCooldown = true;
		}
		if(TEMPOrary.INPUT.isKeyPressed("ESCAPE"))System.exit(0);
		
		if(!TEMPOrary.INPUT.isKeyPressed("LEFT")) player.setLeft(false);
		if(!TEMPOrary.INPUT.isKeyPressed("RIGHT"))player.setRight(false);
		if(!TEMPOrary.INPUT.isKeyPressed("DOWN")) player.setDown(false);
		if(!TEMPOrary.INPUT.isKeyPressed("SPACE"))player.setJumping(false);
		if(!TEMPOrary.INPUT.isKeyPressed("F3")) debugCooldown = false;
		
		stage.act(delta);
	}
	
	public TiledMap getMap() {
		return map;
	}
	public TiledMapTileLayer getLayer() {
		return layer;
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		map.dispose();
	}
	
	public static TestLevel getInstance() {
		return instance;
	}

}
