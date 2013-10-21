package dev.andcamp.spaceassault.entities;

import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IShape;

public interface Bullet {
	public float getX();
	public float getY();
	public float getHeight();
	public float getWidth();
	public IEntity getEntity();
	public boolean collidesWith(IShape shape);
	public IShape getShape();
}
