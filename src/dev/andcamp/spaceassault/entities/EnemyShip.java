package dev.andcamp.spaceassault.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class EnemyShip extends AnimatedSprite implements Enemy{
	
	private PhysicsHandler mPhysicsHandler;

	public EnemyShip(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		animate(200);
		mPhysicsHandler = new PhysicsHandler(this);
		registerUpdateHandler(mPhysicsHandler);
		mPhysicsHandler.setVelocityY(75);
	}

	@Override
	public IEntity getEntity() {
		return this;
	}

	@Override
	public IShape getShape() {
		return this;
	}

	
}
