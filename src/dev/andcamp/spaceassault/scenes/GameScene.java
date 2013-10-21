package dev.andcamp.spaceassault.scenes;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import dev.andcamp.spaceassault.ResourceManager;
import dev.andcamp.spaceassault.ResourceManager.Textures;
import dev.andcamp.spaceassault.Settings;
import dev.andcamp.spaceassault.entities.Bullet;
import dev.andcamp.spaceassault.entities.Enemy;
import dev.andcamp.spaceassault.entities.EnemyShip;
import dev.andcamp.spaceassault.entities.Player;
import dev.andcamp.spaceassault.entities.PlayerBullet;
import dev.andcamp.spaceassault.entities.explosions.SmallExplosion;

public class GameScene extends BaseScene {
	
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private Sprite mButtonFire;
	
	private Player mPlayer;
	private TimerHandler mBulletHandler, mSwarmHandler;
	private UpdateHandler mUpdateHandler;
	private TMXLayer mLayerMap; 
	
	private ArrayList<Enemy> mEnemies;
	private ArrayList<Bullet> mPlayerBullets;
	private ArrayList<SmallExplosion> mExplosions;

	public GameScene(BaseGameActivity activity) {
		this(activity, null);
	}
	
	public GameScene(BaseGameActivity activity, Engine engine){
		super(activity, engine);
	}

	@Override
	public void initialize(Bundle arguments) {
		mEnemies = new ArrayList<Enemy>();
		mPlayerBullets = new ArrayList<Bullet>();
		mExplosions = new ArrayList<SmallExplosion>();
		
		mPlayer.setPosition(50, Settings.CAMERA_HEIGHT - 64);
		createSwarmEnemies();
		mLayerMap.setPosition(0, Settings.CAMERA_HEIGHT - mLayerMap.getHeight());
		
		mUpdateHandler = new UpdateHandler(30);
		registerUpdateHandler(mUpdateHandler);
	}

	@Override
	public void loadResources() {
		ResourceManager.getInstance().loadTexture(SceneType.GAME);
	}

	@Override
	public void createScene() {
		setBackground(new Background(1, .4f, 1));
		setOnAreaTouchTraversalFrontToBack();
		
		final ResourceManager manager = ResourceManager.getInstance();
		
		mLayerMap = manager.getMap().getTMXLayers().get(0);
		attachChild(mLayerMap);
		
		mPlayer = new Player(50, Settings.CAMERA_HEIGHT - 64, manager.getTiledTexture(Textures.PLAYER), mActivity.getVertexBufferObjectManager());
		attachChild(mPlayer);
		
		settingUpControls();
		mButtonFire = new Sprite(Settings.CAMERA_WIDTH - 64, Settings.CAMERA_HEIGHT - 64,	manager.getTexture(Textures.BUTTON_FIRE), mActivity.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch (pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					mEngine.registerUpdateHandler(getBulletHandler());
					break;
				case TouchEvent.ACTION_MOVE: break;
				default:
					mEngine.unregisterUpdateHandler(mBulletHandler);
				}
				return true;
			}
			
		};
		mButtonFire.setScale(.8f);
		attachChild(mButtonFire);
		
		registerTouchArea(mButtonFire);
		setTouchAreaBindingOnActionDownEnabled(true);
	}
	
	private void createSwarmEnemies(){
		mSwarmHandler = new TimerHandler(4f,true, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				//mEngine.unregisterUpdateHandler(pTimerHandler);
				Log.i(Settings.TAG, "asdasda");
				if(mEnemies != null){
					final ResourceManager manager = ResourceManager.getInstance();
					ArrayList<Enemy> enemies = new ArrayList<Enemy>();
					enemies.add(new EnemyShip((float)Math.random()*200, -64, manager.getTiledTexture(Textures.ENEMY), 
						mActivity.getVertexBufferObjectManager()));
					enemies.add(new EnemyShip((float)Math.random()*200, -130, manager.getTiledTexture(Textures.ENEMY), 
						mActivity.getVertexBufferObjectManager()));
					for(Enemy enemy : enemies){
						attachChild(enemy.getEntity());
					}
				
					mEnemies.addAll(enemies);
					enemies = null;
				}
			}
		});
		mEngine.registerUpdateHandler(mSwarmHandler);
	}
	
	private void settingUpControls(){
		final ResourceManager manager = ResourceManager.getInstance();
		mDigitalOnScreenControl = new DigitalOnScreenControl(10, Settings.CAMERA_HEIGHT - manager.getTexture(Textures.CONTROL_BASE).getHeight() - 10, getActivity().mCamera, manager.getTexture(Textures.CONTROL_BASE), 
				manager.getTexture(Textures.CONTROL_KNOB), 0.1f, mActivity.getVertexBufferObjectManager(), new IOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				mPlayer.onControlChange(pValueX, pValueY);
			}
		});
		final Sprite control = mDigitalOnScreenControl.getControlBase();
		control.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		control.setAlpha(0.5f);
		control.setScaleCenter(0, 128);
		control.setScale(0.5f);
		mDigitalOnScreenControl.getControlKnob().setScale(0.5f);
		mDigitalOnScreenControl.refreshControlKnobPosition();

		setChildScene(mDigitalOnScreenControl);
	}
	
	private void removeSprite(IEntity entity){
		final EngineLock engineLock = mEngine.getEngineLock();
		engineLock.lock();
		detachChild(entity);
		entity.dispose();
		engineLock.unlock();
	}
	
	private class UpdateHandler implements IUpdateHandler {
		private final float minTime;
		private float timeEllapsed;
		
		public UpdateHandler(int fps){
			minTime = 1 /fps;
		}
		
		@Override
		public void reset() {
		}
		
		@Override
		public void onUpdate(float pSecondsElapsed) {
			timeEllapsed += pSecondsElapsed;
			if(timeEllapsed >= minTime){
				if(mLayerMap.getY()<0)
					mLayerMap.setY(mLayerMap.getY()+1);
				deleteEndedExplosion();
				deleteNonVisibleBullets();
				checkCollisionEnemies();
				timeEllapsed = 0;
			}
		}
	}
	
	private void deleteEndedExplosion(){
		ArrayList<SmallExplosion> readyToDelete = new ArrayList<SmallExplosion>();
		for(SmallExplosion explosion : mExplosions){
			if(!explosion.isAnimationRunning()){
				removeSprite(explosion);
				readyToDelete.add(explosion);
			}
		}
		mExplosions.removeAll(readyToDelete);
		readyToDelete = null;
	}
	
	private void checkCollisionEnemies(){
		ArrayList<Enemy> readyToDelete = new ArrayList<Enemy>();
		ArrayList<Bullet> bulletsReadyToDelete = new ArrayList<Bullet>();
		for(Enemy enemy: mEnemies){
			if(enemy.getY() > Settings.CAMERA_HEIGHT){
				removeSprite(enemy.getEntity());
				readyToDelete.add(enemy);
				break;
			}
			for(Bullet bullet: mPlayerBullets){
				if(bullet.collidesWith(enemy.getShape())){
					removeSprite(bullet.getEntity());
					removeSprite(enemy.getEntity());
					readyToDelete.add(enemy);
					bulletsReadyToDelete.add(bullet);
					attachExplosion(enemy);
					break;
				}
			}
			if(!bulletsReadyToDelete.isEmpty()){
				mPlayerBullets.removeAll(bulletsReadyToDelete);
				bulletsReadyToDelete.clear();
			}
		}
		mEnemies.removeAll(readyToDelete);
		readyToDelete = null;
		bulletsReadyToDelete = null;
	}
	
	private void attachExplosion(Enemy enemy){
		final SmallExplosion explosion = new SmallExplosion(enemy, mActivity.getVertexBufferObjectManager());
		mExplosions.add(explosion);
		attachChild(explosion);
	}
	
	private void deleteNonVisibleBullets(){
		ArrayList<Bullet> readyToDelete = new ArrayList<Bullet>();
		for(Bullet bullet: mPlayerBullets){
			if(bullet.getY() < -bullet.getHeight()){
				removeSprite(bullet.getEntity());
				readyToDelete.add(bullet);
			}
		}
		mPlayerBullets.removeAll(readyToDelete);
		readyToDelete = null;
	}
	
	private TimerHandler getBulletHandler(){
		if(mBulletHandler == null){
			mBulletHandler = new TimerHandler(.2f, true, new ITimerCallback() {
				
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					final PlayerBullet bullet = new PlayerBullet(mPlayer, mActivity.getVertexBufferObjectManager());
					mPlayerBullets.add(bullet);
					attachChild(bullet);
				}
			});
		}
		
		return mBulletHandler;
	}
	
	private void dettachAll(){
		for(Enemy enemy : mEnemies){
			removeSprite(enemy.getEntity());
		}
		mEnemies = null;
		for(Bullet bullet : mPlayerBullets){
			removeSprite(bullet.getEntity());
		}
		mPlayerBullets = null;
		for(SmallExplosion explosion : mExplosions){
			removeSprite(explosion);
		}
		mExplosions = null;
	}

	@Override
	public void destroy() {
		Log.i(Settings.TAG, "destroy");
		dettachAll();
		mSwarmHandler.setAutoReset(false);
		unregisterUpdateHandler(mSwarmHandler);
		unregisterUpdateHandler(mUpdateHandler);
		mSwarmHandler = null;
		mUpdateHandler = null;
	}

}
