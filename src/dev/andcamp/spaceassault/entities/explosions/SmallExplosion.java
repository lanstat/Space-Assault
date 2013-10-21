package dev.andcamp.spaceassault.entities.explosions;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import dev.andcamp.spaceassault.ResourceManager;
import dev.andcamp.spaceassault.ResourceManager.Textures;
import dev.andcamp.spaceassault.entities.Enemy;

public class SmallExplosion extends AnimatedSprite {

	public SmallExplosion(Enemy enemy,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, ResourceManager.getInstance().getTiledTexture(Textures.EXPLOSION), pVertexBufferObjectManager);
		animate(150, false);
		mX = (enemy.getX() + enemy.getWidth()*.5f) - mWidth*.5f;
		mY = (enemy.getY() + enemy.getHeight()*.5f) - mWidth*.5f;
	}

}
