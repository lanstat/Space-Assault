package dev.andcamp.spaceassault.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import dev.andcamp.spaceassault.Settings;

public class Player extends AnimatedSprite{
	
	private PhysicsHandler mPhysicsHandler;

	public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		animate(200);
		mPhysicsHandler = new PhysicsHandler(this);
		registerUpdateHandler(mPhysicsHandler);
	}
	
	public void move(float valueX, float moveY){
		mPhysicsHandler.setVelocity(valueX*75, moveY*75);
	}
	
	public void onControlChange(float valueX, float valueY){
		if (mX <= 10)
			mX = 10;
		if (mX + mWidth >= Settings.CAMERA_WIDTH -10)
			mX = Settings.CAMERA_WIDTH - mWidth - 10;
		
		mPhysicsHandler.setVelocity(valueX * 75, valueY * 75);
	}
}
