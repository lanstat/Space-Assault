package dev.andcamp.spaceassault.entities;

import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IShape;

public interface Enemy {
	
	public float getX();
	public float getY();
	public float getWidth();
	public float getHeight();
	public IEntity getEntity();
	public IShape getShape();
	
	
}
