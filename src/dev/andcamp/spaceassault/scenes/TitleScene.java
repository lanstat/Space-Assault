package dev.andcamp.spaceassault.scenes;

import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import dev.andcamp.spaceassault.ResourceManager;
import dev.andcamp.spaceassault.Settings;
import dev.andcamp.spaceassault.ResourceManager.Fonts;
import dev.andcamp.spaceassault.ResourceManager.Textures;
import android.os.Bundle;

public class TitleScene extends BaseScene implements IOnSceneTouchListener{

	public TitleScene(BaseGameActivity activity) {
		super(activity);
	}

	@Override
	public void initialize(Bundle lclsArguments) {
	}

	@Override
	public void loadResources() {
		ResourceManager.getInstance().loadTexture(SceneType.TITLE);
	}

	@Override
	public void createScene() {
		setBackground(new Background(0f, 0f, 0f));
		final ResourceManager manager =  ResourceManager.getInstance();
		final Sprite background = new Sprite(0, 0, manager.
				getTexture(Textures.TITLE_BACKGROUND), mActivity.getVertexBufferObjectManager());
		background.setSize(Settings.CAMERA_WIDTH, Settings.CAMERA_HEIGHT);
		
		final Text message = new Text(0, 0, manager.getFont(Fonts.GAME_WHITE), "PRESS SCREEN"
				, mActivity.getVertexBufferObjectManager());
		message.setPosition((Settings.CAMERA_WIDTH*.5f)-(message.getWidth()*.5f), Settings.CAMERA_HEIGHT*.75f);
		final LoopEntityModifier modifier = new LoopEntityModifier(new SequenceEntityModifier(
				new FadeOutModifier(1f),
				new DelayModifier(0.5f),
				new FadeInModifier(1f)));
		message.registerEntityModifier(modifier);
		
		attachChild(background);
		attachChild(message);
		setOnSceneTouchListener(this);
	}

	@Override
	public void destroy() {
	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		getActivity().manager.addCurrentSceneToStack();
		getActivity().manager.initialize(SceneType.GAME, null);
		getActivity().manager.setCurrentScene(SceneType.GAME);
		return false;
	}

}
