package com.github.dublekfx.TEMPOrary.entities.friendly;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
	
public class Dragon extends Player {

	public Dragon(Texture texture, Screen level) {
		super(texture, level);
		this.health = 100;
		
		spriteWidth = spriteHeight = 30;
		hitBoxWidth = 20;
		hitBoxHeight = 30;
		hitBoxOffsetX = 5;
		hitBoxOffsetY = 0;
		
		moveSpeed = 3;
		maxMoveSpeed = 3;
		jumpVel = 6;
		stopJumpVel = 1;
		
		TextureRegion[][] region = TextureRegion.split(texture, spriteWidth, spriteHeight);

		TextureRegion[] idleRegion = new TextureRegion[2];
		System.arraycopy(region[0], 0, idleRegion, 0, 2);

		TextureRegion[] walkingRegion = new TextureRegion[8];
		System.arraycopy(region[1], 0, walkingRegion, 0, 8);

		TextureRegion[] jumpingRegion = new TextureRegion[1];
		System.arraycopy(region[2], 0, jumpingRegion, 0, 1);

		idling = new Animation(0.25f, idleRegion);
		walking = new Animation(0.05f, walkingRegion);
		jumping = new Animation(.1f, jumpingRegion);

		this.hitBox = new Rectangle(this.getX() + hitBoxOffsetX, this.getY() + hitBoxOffsetY, hitBoxWidth, hitBoxHeight);
		
		this.currentState = AnimationState.IDLING;
		left = right = isJumping = isGrounded = isFalling = false;
		this.setPosition(40, 40);
	}
}
