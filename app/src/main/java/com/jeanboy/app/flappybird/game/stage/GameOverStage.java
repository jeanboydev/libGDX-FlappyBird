package com.jeanboy.app.flappybird.game.stage;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jeanboy.app.flappybird.game.MainGame;
import com.jeanboy.app.flappybird.game.Res;
import com.jeanboy.app.flappybird.game.actor.ResultGroup;
import com.jeanboy.app.flappybird.game.actor.framework.ImageActor;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class GameOverStage extends BaseStage {

    private ImageActor gameOverActor;

    private ResultGroup resultGroup;

    private ImageButton restartButton;

    private Sound restartSound;

    public GameOverStage(MainGame mainGame, Viewport viewport) {
        super(mainGame, viewport);
        init();
    }

    private void init() {
        restartSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_RESTART, Sound.class);

        gameOverActor = new ImageActor(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_OVER));
        gameOverActor.setCenterX(getWidth() / 2);
        gameOverActor.setTopY(getHeight() - 150);
        addActor(gameOverActor);

        resultGroup = new ResultGroup(getMainGame());
        resultGroup.setCenterX(getWidth() / 2);
        resultGroup.setTopY(gameOverActor.getY() - 30);
        addActor(resultGroup);

        restartButton = new ImageButton(
                new TextureRegionDrawable(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_START_01_TO_02, 1)),
                new TextureRegionDrawable(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_START_01_TO_02, 2)));
        restartButton.setX(getWidth() / 2 - restartButton.getWidth() / 2);
        restartButton.setY(resultGroup.getY() - 30 - restartButton.getHeight());
        addActor(restartButton);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // 按钮点击, 播放音效
                restartSound.play();
                // 重新开始游戏
                getMainGame().getGameScreen().restartReadyGame();
            }
        });
    }

    public void setCurrentScore(int currScore) {
        resultGroup.updateCurrScore(currScore);
    }
}
