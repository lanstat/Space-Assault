package dev.andcamp.spaceassault.scenes;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;

import dev.andcamp.spaceassault.ResourceManager;
import dev.andcamp.spaceassault.Settings;
import dev.andcamp.spaceassault.ResourceManager.Textures;
import android.os.Bundle;

public class SplashScene extends BaseScene{

	Sprite mLogo;
	AlphaModifier mFade;
	boolean mShow = true;
	
	public SplashScene(BaseGameActivity activity) {
		super(activity);
	}

	@Override
	public void initialize(Bundle lclsArguments) {	
	}

	@Override
	public void loadResources() {
		ResourceManager.getInstance().loadTexture(SceneType.SPLASH);
	}

	@Override
	public void createScene() {
		setBackground(new Background(1f, 1f, 1f));
		mLogo = new Sprite(0, 0, ResourceManager.getInstance().getTexture(Textures.LOGO), 
				mActivity.getVertexBufferObjectManager());
		mLogo.setPosition((Settings.CAMERA_WIDTH*.5f)-(mLogo.getWidth()*.5f), (Settings.CAMERA_HEIGHT*.5f)-(mLogo.getWidth()*.5f));
		mLogo.setAlpha(0f);
		mFade = new FadeInModifier(3f);
		mLogo.registerEntityModifier(mFade);
		attachChild(mLogo);
		registerUpdateHandler(new TimerHandler(.1f, true, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				if(mShow){
					if(mFade.isFinished()){
						getActivity().manager.loadAllResources();
						getActivity().manager.createAllScenes();
						mLogo.unregisterEntityModifier(mFade);
						mFade = new FadeOutModifier(3f);
						mLogo.registerEntityModifier(mFade);
						mShow = false;
					}
				}else{
					if(mFade.isFinished()){
						pTimerHandler.setAutoReset(false);
						unregisterUpdateHandler(pTimerHandler);
						//getActivity().manager.initialize(SceneType.GAME, null);
						getActivity().manager.setCurrentScene(SceneType.TITLE);
					}
				}
			}
		}));
		
	}

	@Override
	public void destroy() {
	}
}
