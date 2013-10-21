package dev.andcamp.spaceassault.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import dev.andcamp.spaceassault.ResourceManager;
import dev.andcamp.spaceassault.ResourceManager.Textures;

public class PlayerBullet extends Sprite implements Bullet{

	private PhysicsHandler mPhysicsHandler;

	public PlayerBullet(Player player,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, ResourceManager.getInstance().getTexture(Textures.PLAYER_BULLET), pVertexBufferObjectManager);
		mPhysicsHandler = new PhysicsHandler(this);
		registerUpdateHandler(mPhysicsHandler);
		mPhysicsHandler.setVelocityY(-90);
		mX = (player.getX()+player.getWidth()*.5f) - mWidth*.5f;
		mY = player.getY();
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
