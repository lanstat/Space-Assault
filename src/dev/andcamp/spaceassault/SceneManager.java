package dev.andcamp.spaceassault;

import java.util.Stack;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.os.Bundle;
import dev.andcamp.spaceassault.scenes.BaseScene;
import dev.andcamp.spaceassault.scenes.GameScene;
import dev.andcamp.spaceassault.scenes.SceneType;
import dev.andcamp.spaceassault.scenes.SplashScene;
import dev.andcamp.spaceassault.scenes.TitleScene;

public class SceneManager {
	private SceneType mCurrentScene;
	private Engine engine;
	private Stack<SceneType> mStackScene;
	private BaseScene[] mScenes;
	
	public SceneManager(BaseGameActivity activity, Engine engine, Camera camera) {
		this.engine = engine;
		mStackScene = new Stack<SceneType>();
		mScenes = new BaseScene[SceneType.values().length];
		mCurrentScene = SceneType.SPLASH;
		init(activity, engine);
	}
	
	private void init(BaseGameActivity activity, Engine engine){
		mScenes[SceneType.SPLASH.ordinal()] = new SplashScene(activity);
		mScenes[SceneType.GAME.ordinal()] = new GameScene(activity, engine);
		mScenes[SceneType.TITLE.ordinal()] = new TitleScene(activity);
	}

	public void loadSplashSceneResources(){
		mScenes[SceneType.SPLASH.ordinal()].loadResources();
	}
	
	public void loadResources(SceneType scene){
		mScenes[scene.ordinal()].loadResources();
	}
	
	public void loadAllResources(){
		ResourceManager.getInstance().loadFonts();
		mScenes[SceneType.TITLE.ordinal()].loadResources();
		mScenes[SceneType.GAME.ordinal()].loadResources();
	}
	
	public void createAllScenes(){
		mScenes[SceneType.TITLE.ordinal()].createScene();
		mScenes[SceneType.GAME.ordinal()].createScene();
	}
	
	public Scene createSplashScene(){
		mScenes[SceneType.SPLASH.ordinal()].createScene();
		
		return mScenes[SceneType.SPLASH.ordinal()];
	}
	
	public void createScene(SceneType scene){
		mScenes[scene.ordinal()].createScene();
	}
	
	public void initialize(SceneType scene, Bundle bundle){
		mScenes[scene.ordinal()].initialize(bundle);
	}
	
	public SceneType getCurrentSceneType() {
		return mCurrentScene;
	}
	
	public Scene getCurrentScene(){
		return selectScene(mCurrentScene);
	}
	
	public void setCurrentScene(SceneType scene) {
		mCurrentScene = scene;
		engine.setScene(selectScene(scene));
	}
	
	public void addCurrentSceneToStack(){
		mStackScene.push(mCurrentScene);
	}
	
	public void destroy(){
		mScenes[mCurrentScene.ordinal()].destroy();
	}
	
	private Scene selectScene(SceneType scene){
		Scene selectScene = mScenes[scene.ordinal()];
		return selectScene;
	}
	
	public boolean returnLastScene(){
		if(!mStackScene.isEmpty()){
			mScenes[mCurrentScene.ordinal()].destroy();
			mCurrentScene = mStackScene.pop();
			engine.setScene(mScenes[mCurrentScene.ordinal()]);
			return true;
		}
		return false;
	}
}
