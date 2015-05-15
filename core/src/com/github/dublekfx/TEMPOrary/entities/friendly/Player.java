package com.github.dublekfx.TEMPOrary.entities.friendly;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.github.dublekfx.TEMPOrary.TEMPOrary;
import com.github.dublekfx.TEMPOrary.entities.Entity;
import com.github.dublekfx.TEMPOrary.screens.levels.TestLevel;

public class Player extends Entity {
	
	enum AnimationState {
		IDLING, WALKING, JUMPING
	}
	
	protected AnimationState currentState;
	protected Animation idling;
	protected Animation walking;
	protected Animation jumping;
	protected Animation doubleJumping;
	protected float stateTime;
	protected boolean left, right, up, down, isJumping, isGrounded, isFalling;
	
	protected float moveSpeed;
	protected float maxMoveSpeed;
	protected float jumpVel;
	protected float doubleJumpVel;
	protected float stopJumpVel;
	
	//DoubleJump
	protected boolean canDoubleJump = false;
	protected boolean doubleJumpReady;
	protected boolean upReleasedInAir;

	public Player(Texture texture, Screen level) {
		super(texture, level);
		this.stateTime = 0;
	}
	
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}
	
	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public boolean isGrounded() {
		return isGrounded;
	}

	public void setGrounded(boolean isGrounded) {
		this.isGrounded = isGrounded;
	}

	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		TextureRegion frame = null;
		switch(currentState) {
		case IDLING:
			frame = idling.getKeyFrame(stateTime, true);
			break;
		case WALKING:
			frame = walking.getKeyFrame(stateTime, true);
			break;
		case JUMPING:
			frame = jumping.getKeyFrame(stateTime, true);
			break;
		default:
			break;
		}
		if(this.facingRight) {
			batch.draw(frame, this.getX(), this.getY(), spriteWidth, spriteHeight);
		}
		else {
			batch.draw(frame, this.getX() + spriteWidth, this.getY(), -spriteWidth, spriteHeight);
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.stateTime += delta;
		this.setHitBox(new Rectangle(this.getX() + hitBoxOffsetX, this.getY() + hitBoxOffsetY, hitBoxOffsetX, hitBoxOffsetY));
		updatePosition();
		updateState();
	}

private void updateState() {
		
		if(velocity.x == 0 && velocity.y == 0 && isGrounded) {
			currentState = AnimationState.IDLING;
		}
		else if(velocity.x != 0 && velocity.y == 0 && isGrounded) {
			currentState = AnimationState.WALKING;
		}
		else if(velocity.y != 0 && !isGrounded) {
			currentState = AnimationState.JUMPING;
		}		
	}
	
	public void updatePosition() {
		Gdx.app.debug("[Collision]", "Start: " + this.isJumping + " " + this.isGrounded + " " + this.isFalling);
		int startX, endX, startY, endY;
		Rectangle nextPos = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		TiledMapTileLayer layer = TestLevel.getInstance().getLayer();
		ArrayList<Rectangle> tiles;
		TiledMapTile testTile;
		boolean yInteracted = false;
		
		this.setHitBox(new Rectangle(this.getX() + 5, this.getY(), 20, 30));
		
		//Horizontal Translation
		Gdx.app.debug("[Collision]", "Begin X Velocity Check");
		if(left) {
			velocity.x -= moveSpeed;
			if(velocity.x < -maxMoveSpeed) velocity.x = -maxMoveSpeed;
		}
		if(right) {
			velocity.x += moveSpeed;
			if(velocity.x > maxMoveSpeed) velocity.x = maxMoveSpeed;
		}
		if(!left && !right) {
			velocity.x = 0;
		}
		Gdx.app.debug("[Collision]", "X Velocity: " + velocity.x);
		if (velocity.x > 0) {
			startX = endX = (int)(this.getHitBox().getX() + this.getHitBox().getWidth() + velocity.x) / (int)layer.getTileWidth();
		} else {
			startX = endX = (int)(this.getHitBox().getX() + velocity.x) / (int)layer.getTileWidth();
		}
		startY = (int)(this.getHitBox().getY()) / (int)layer.getTileHeight();
		endY = (int)(this.getHitBox().getY() + this.getHitBox().getHeight()) / (int)layer.getTileHeight();
		Gdx.app.debug("[Collision]", "TileCoords: " + startX + ", " + startY + ", " + endX + ", " + endY);
		tiles = this.getTiles(startX, endX, startY, endY);
		nextPos.x += velocity.x;
		for(Rectangle tile : tiles) {
			if(layer.getCell((int)tile.x, (int)tile.y) != null) {
				testTile = layer.getCell((int)tile.x, (int)tile.y).getTile();
				if(testTile.getProperties().get("Interactable").equals("Wall")) {
					nextPos.x -= velocity.x;
					break;
				}
			}
		}
		//Vertical Translation
		Gdx.app.debug("[Collision]", "Begin Y Velocity Check");
		if(isGrounded) {
			upReleasedInAir = false;
			doubleJumpReady = true;
		}
		if(isJumping && !isFalling) {
			velocity.y = this.jumpVel;
			isFalling = true;
			isGrounded = false;
		}
		if(isFalling) {
			if(canDoubleJump) {
				if(!TEMPOrary.INPUT.isKeyPressed("SPACE")) upReleasedInAir = true;
				if(TEMPOrary.INPUT.isKeyPressed("SPACE") && upReleasedInAir && doubleJumpReady) {
					velocity.y = this.doubleJumpVel;
					doubleJumpReady = false;
				}
			}
			isGrounded = false;
			if(velocity.y < 0) {
				isJumping = false;
			}
			if(velocity.y > 0 && !isJumping) {
				velocity.y -= stopJumpVel;
			}
		}
		if(!isGrounded) {
			velocity.y -= TestLevel.GRAVITY;
		}
		else {
			isFalling = false;
		}
		Gdx.app.debug("[Collision]", "Y Velocity: " + velocity.y);
		if (velocity.y > 0) {
			startY = endY = (int)(this.getHitBox().getY() + this.getHitBox().getHeight() + velocity.y) / (int)layer.getTileHeight();
		}
		else if(velocity.y < 0){
			startY = endY = (int)(this.getHitBox().getY() + velocity.y) / (int)layer.getTileHeight();
		}
		else {
			startY = (int)(this.getHitBox().getY() + velocity.y) / (int)layer.getTileHeight() - 1;
			endY = (int)(this.getHitBox().getY() + velocity.y) / (int)layer.getTileHeight();
		}
		startX = (int)(this.getHitBox().getX()) / (int)layer.getTileWidth();
		endX = (int)(this.getHitBox().getX() + this.getHitBox().getWidth()) / (int)layer.getTileWidth();
		Gdx.app.debug("[Collision]", "TileCoords: " + startX + ", " + startY + ", " + endX + ", " + endY);
		tiles = this.getTiles(startX, endX, startY, endY);
		nextPos.y += velocity.y;
		for(Rectangle tile : tiles) {
			Gdx.app.debug("[Collision]", "Tile: " + tile.x + ", " + tile.y);
			if(layer.getCell((int)tile.x, (int)tile.y) != null) {
				testTile = layer.getCell((int)tile.x, (int)tile.y).getTile();
				if(testTile.getProperties().get("Interactable").equals("Wall")) {
					Gdx.app.debug("[Collision]", "Wall Interact");
					if(velocity.y < 0) {
						nextPos.y = (tile.y + 1) * 30;
						velocity.y = 0;
						this.isGrounded = true;
						this.isFalling = false;
						yInteracted = true;
					}
					else if(velocity.y > 0) {
						nextPos.y = (tile.y - 1) * 30;
						velocity.y = 0;
						nextPos.x += velocity.x;
					}
					break;
				}
				else if(testTile.getProperties().get("Interactable").equals("Platform")) {
					Gdx.app.debug("[Collision]", "Platform Interact");
					if(velocity.y < 0) {
						if(!this.isGrounded && this.isFalling) {
							Gdx.app.debug("[Collision]", "Platform Approach");
							Gdx.app.debug("[Collision]", "" + tile.y * layer.getTileHeight());
							if(tile.y * layer.getTileHeight() + layer.getTileHeight() > this.getHitBox().getY()) {
								Gdx.app.debug("[Collision]", "Drop Prep");
							}
							else {
								nextPos.y = (tile.y + 1) * 30;
								velocity.y = 0;
								this.isGrounded = true;
								this.isFalling = false;
							}
						}
						else if(this.isGrounded){
							Gdx.app.debug("[Collision]", "Platform Rest");
							nextPos.y = (tile.y + 1) * 30;
							this.isFalling = false;
						}
						break;
					}
					else if(velocity.y == 0) {
						//
						if(this.down) {
							Gdx.app.debug("[Collision]", "Platform Dropping");
							this.isFalling = true;
							this.isGrounded = false;
							velocity.y -= TestLevel.GRAVITY;
							nextPos.y += velocity.y;
							break;
						}
						else {
							Gdx.app.debug("[Collision]", "Platform Resting");
							this.isFalling = false;
							yInteracted = true;
							break;
						}
					}
				}
				else {
					Gdx.app.debug("[Collision]", "No Interaction");
					this.isFalling = true;
					break;
				}
			}
			else {
				Gdx.app.debug("[Collision]", "Null Tile");
				this.isFalling = true;
				break;
			}
		}
		Gdx.app.debug("[Collision]", "End: " + this.isJumping + " " + this.isGrounded + " " + this.isFalling);
		Gdx.app.debug("[Collision]", "NextPos: " + nextPos.x + ", " + nextPos.y + "\n");
		this.setPosition(nextPos.x, nextPos.y);
	}
	
	private ArrayList<Rectangle> getTiles(int startX, int endX, int startY, int endY) {
		ArrayList<Rectangle> tiles = new ArrayList<Rectangle>();
		TiledMapTileLayer layer = TestLevel.getInstance().getLayer();
		
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				Rectangle cell = new Rectangle(x, y, 1, 1);
				if (cell != null) {
					tiles.add(cell);
				}
			}
		}
//		Gdx.app.debug("[Collision]", "getTiles: " + tiles.toString());
		return tiles;
	}
	
}
