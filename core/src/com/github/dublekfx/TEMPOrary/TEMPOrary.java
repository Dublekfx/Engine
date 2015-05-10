package com.github.dublekfx.TEMPOrary;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.dublekfx.TEMPOrary.input.InputManager;
import com.github.dublekfx.TEMPOrary.screens.levels.TestLevel;

public class TEMPOrary extends Game {
	
	public static InputManager INPUT = new InputManager();
	
	private SpriteBatch batch;
	private Screen level;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Gdx.app.LOG_INFO);
		Gdx.input.setInputProcessor(INPUT);
		level = new TestLevel(this);
		this.setScreen(level);
	}

	@Override
	public void render () {
		super.render();
	}
	
	public void dispose() {
		level.dispose();
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
}
