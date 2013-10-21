package dev.andcamp.spaceassault.scenes;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.activity.BaseGameActivity;

import dev.andcamp.spaceassault.MainActivity;

public abstract class BaseScene extends Scene implements IStateListener{
	protected BitmapTextureAtlas mBitmapTextureAtlas;
	protected BaseGameActivity mActivity;
	protected Engine mEngine;
	
	
	public BaseScene(BaseGameActivity activity){
		this(activity, null);
	}
	
	public BaseScene(BaseGameActivity activity, Engine engine){
		mActivity = activity;
		mEngine = engine;
	}
	
	public MainActivity getActivity(){
		return (MainActivity)mActivity;
	}
}
