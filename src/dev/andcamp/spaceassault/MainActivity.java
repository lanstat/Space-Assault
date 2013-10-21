package dev.andcamp.spaceassault;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity {
	
	public BoundCamera mCamera;
	public SceneManager manager;

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new BoundCamera(0, 0, Settings.CAMERA_WIDTH, Settings.CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
				new FillResolutionPolicy(), mCamera);
		
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		/*if(MultiTouch.isSupported(this)) {
			if(MultiTouch.isSupportedDistinct(this)) {
				Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
		}*/
		
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		ResourceManager.getInstance().sendParameters(this);
		manager = new SceneManager(this, getEngine(), mCamera);
		manager.loadSplashSceneResources();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		pOnCreateSceneCallback.onCreateSceneFinished(manager.createSplashScene());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onBackPressed() {
		if(!manager.returnLastScene()){
			manager.destroy();
			ResourceManager.reset();
			super.onBackPressed();
		}
	}
	
	

}