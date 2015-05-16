package com.github.dublekfx.TEMPOrary.entities;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.dublekfx.TEMPOrary.utilities.Options;
import data_structures.Vector2D;

public class Entity extends Actor {

	protected Texture texture;
	protected Screen level;

	protected int spriteWidth, spriteHeight;
	protected int hitBoxWidth, hitBoxHeight, hitBoxOffsetX, hitBoxOffsetY;
	
	protected Rectangle hitBox;

	protected Vector2D velocity;
	
	protected int health;
	
	protected boolean facingRight;
	protected boolean onGround;
	
	public Entity(Texture texture, Screen level) {
		this.texture = texture;
		this.level = level;
		hitBox = new Rectangle();
		velocity = new Vector2D();
		this.facingRight = true;
		this.onGround = true;
	}

	public Rectangle getHitBox() {
		return hitBox;
	}

	public void setHitBox(Rectangle hitBox) {
		this.hitBox = hitBox;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(Options.DEBUG) {
			this.debugDraw(batch, parentAlpha);
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(velocity.x < 0) {
			facingRight = false;
		}
		else if(velocity.x > 0) {
			facingRight = true;
		}
	}
	
	private void debugDraw(Batch batch, float parentAlpha) {
		batch.end();
		ShapeRenderer shaper = new ShapeRenderer();
		shaper.setProjectionMatrix(batch.getProjectionMatrix());
		shaper.begin(ShapeType.Line);
		shaper.setColor(Color.RED);
		shaper.rect(this.getX(), this.getY(), spriteWidth, spriteHeight);
		shaper.setColor(Color.BLUE);
		shaper.rect(this.getX() + hitBoxOffsetX, this.getY() + hitBoxOffsetY, hitBoxWidth, hitBoxHeight);
		shaper.end();
		batch.begin();
	}
}
