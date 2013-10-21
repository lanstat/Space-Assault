package dev.andcamp.spaceassault;

import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import dev.andcamp.spaceassault.scenes.SceneType;
import android.graphics.Color;

public class ResourceManager {
	private static ResourceManager INSTANCE;
    private BaseGameActivity mContext;
    
    private BitmapTextureAtlas mAtlas, mSplashAtlas, fontAtlas;
	private ITextureRegion[] mRegion;
	private TMXTiledMap mTMXTiledMap;
	
	private Font[] mFonts;
	
	private BitmapTextureAtlas mOnScreenControlTexture;
	
	public enum Textures{
		PLAYER,
		PLAYER_BULLET,
		ENEMY,
		ENEMY_BULLET,
		BOSS,
		EXPLOSION,
		CONTROL_BASE,
		CONTROL_KNOB,
		BUTTON_FIRE,
		LOGO,
		TITLE_BACKGROUND
	}
	
	public enum Maps{
		LVL1
	}
	
	public enum Fonts{
		GAME_WHITE
	}
	
	private ResourceManager(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mRegion = new ITextureRegion[Textures.values().length];
		mFonts = new Font[Fonts.values().length];
	}
    
	public synchronized static ResourceManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ResourceManager();
		}
		return INSTANCE;
	}
	
	public void sendParameters(BaseGameActivity mContext){
		this.mContext = mContext;
	}
	
	public TextureRegion getTexture(Textures which){
		return (TextureRegion)mRegion[which.ordinal()];
	}
	
	public TiledTextureRegion getTiledTexture(Textures which){
		return (TiledTextureRegion)mRegion[which.ordinal()];
	}
	
	public TMXTiledMap getMap(){
		return mTMXTiledMap;
	}
	
	public void loadFonts(){
		FontFactory.setAssetBasePath("fonts/");
		fontAtlas = new BitmapTextureAtlas(mContext.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mFonts[Fonts.GAME_WHITE.ordinal()] = FontFactory.createFromAsset(mContext.getFontManager(), fontAtlas, 
				mContext.getAssets(), "pf_tempesta_five.ttf", 16, true, Color.WHITE);
		mFonts[Fonts.GAME_WHITE.ordinal()].load();
	}
	
	public Font getFont(Fonts font){
		return mFonts[font.ordinal()];
	}
	
	public void loadTexture(SceneType type){
		switch (type) {
		case TITLE:
			break;
		case GAME:
			loadGameGraphics();
			break;
		case SPLASH:
			loadSplashGraphics();
			break;
		default:
			break;
		}
	}
	
	private void loadGameGraphics(){
		mAtlas = new BitmapTextureAtlas(mContext.getTextureManager(), 960, 800, TextureOptions.BILINEAR);
		mRegion[Textures.BOSS.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlas, mContext.getAssets(), "boss.png", 0, 0, 2, 2);
		mRegion[Textures.EXPLOSION.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlas, mContext.getAssets(), "explosion2.png", 180, 0, 4, 2);
 		mRegion[Textures.PLAYER.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlas, mContext.getAssets(), "ship2.png", 180, 120, 4, 1);
 		mRegion[Textures.ENEMY.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlas, mContext.getAssets(), "enemy2.png", 180, 192, 4, 1);
		mRegion[Textures.PLAYER_BULLET.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlas, mContext.getAssets(), "bullet.png", 420, 0);
		mRegion[Textures.ENEMY_BULLET.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlas, mContext.getAssets(), "bullet2.png", 420, 34);
		mRegion[Textures.BUTTON_FIRE.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlas, mContext.getAssets(), "bA.png", 420, 68);
		mRegion[Textures.TITLE_BACKGROUND.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlas, mContext.getAssets(), "background.png", 480, 0);
		
		mAtlas.load();
		
		mOnScreenControlTexture = new BitmapTextureAtlas(mContext.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		mRegion[Textures.CONTROL_BASE.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, mContext, "onscreen_control_base.png", 0, 0);
		mRegion[Textures.CONTROL_KNOB.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, mContext, "onscreen_control_knob.png", 128, 0);
		mOnScreenControlTexture.load();
		
		loadMap(Maps.LVL1);
	}
	
	private void loadSplashGraphics(){
		mSplashAtlas = new BitmapTextureAtlas(mContext.getTextureManager(), 200, 200, TextureOptions.BILINEAR);
		mRegion[Textures.LOGO.ordinal()] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSplashAtlas, mContext.getAssets(), "logo.png", 0, 0);
		mSplashAtlas.load();
	}
	
	private void loadMap(Maps lvl){
		final TMXLoader tmxLoader;
		try {
			tmxLoader = new TMXLoader(mContext.getAssets(), mContext.getTextureManager(), mContext.getVertexBufferObjectManager());
			switch (lvl) {
			case LVL1:
				mTMXTiledMap = tmxLoader.loadFromAsset("tmx/map.tmx");
				break;
			}
		} catch (TMXLoadException e) {
			mTMXTiledMap = null;
		}
	}

	public static void reset() {
		INSTANCE = null;
	}
}

