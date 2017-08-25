package com.jeanboy.app.flappybird.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jeanboy.app.flappybird.GameScreen;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class MainGame extends Game {

    public static final String TAG = "FlappyBird";
    public static final boolean SHOW_FPS = true;

    private float worldWidth;
    private float worldHeight;
    private AssetManager assetManager;
    private TextureAtlas atlas;
    private GameScreen gameScreen;
    private BitmapFont fpsBitmapFont;
    private FPSDebug fpsDebug;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        worldWidth = Res.FIX_WORLD_WIDTH;
        worldHeight = Gdx.graphics.getHeight() * worldWidth / Gdx.graphics.getWidth();

        Gdx.app.log(TAG, "World Size: " + worldWidth + " * " + worldHeight);

        assetManager = new AssetManager();

        assetManager.load(Res.Atlas.ATLAS_PATH, TextureAtlas.class);
        assetManager.load(Res.Audios.AUDIO_DIE, Sound.class);
        assetManager.load(Res.Audios.AUDIO_HIT, Sound.class);
        assetManager.load(Res.Audios.AUDIO_TOUCH, Sound.class);
        assetManager.load(Res.Audios.AUDIO_RESTART, Sound.class);
        assetManager.load(Res.Audios.AUDIO_SCORE, Sound.class);

        assetManager.load(Res.FPS_BITMAP_FONT_PATH, BitmapFont.class);

        assetManager.finishLoading();

        atlas = assetManager.get(Res.Atlas.ATLAS_PATH, TextureAtlas.class);
        fpsBitmapFont = assetManager.get(Res.FPS_BITMAP_FONT_PATH, BitmapFont.class);

        gameScreen = new GameScreen(this);

        setScreen(gameScreen);

        if (SHOW_FPS) {
            fpsDebug = new FPSDebug();
            fpsDebug.init(fpsBitmapFont, 24);
        }
    }

    @Override
    public void render() {
        // 黑色清屏
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();

        // 判断是否需要渲染帧率
        if (SHOW_FPS) {
            fpsDebug.render();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        // 应用退出时需要手动销毁场景
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        // 应用退出时释放资源
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (SHOW_FPS) {
            fpsDebug.dispose();
        }
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }
}
