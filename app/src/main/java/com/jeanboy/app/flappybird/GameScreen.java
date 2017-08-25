package com.jeanboy.app.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jeanboy.app.flappybird.game.MainGame;
import com.jeanboy.app.flappybird.game.stage.GameOverStage;
import com.jeanboy.app.flappybird.game.stage.GameStage;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class GameScreen extends ScreenAdapter {

    private MainGame mainGame;
    private GameStage gameStage;

    private GameOverStage gameOverStage;

    public GameScreen(MainGame mainGame) {
        this.mainGame = mainGame;

        init();
    }

    private void init() {
        gameStage = new GameStage(mainGame, new StretchViewport(mainGame.getWorldWidth(), mainGame.getWorldHeight()));

        gameOverStage = new GameOverStage(mainGame, new StretchViewport(mainGame.getWorldWidth(), mainGame.getWorldHeight()));
        gameOverStage.setVisible(false);

        //将输入处理设置到主游戏舞台
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (gameStage.isVisible()) {
            gameStage.act();
            gameStage.draw();
        }

        if (gameOverStage.isVisible()) {
            gameOverStage.act();
            gameOverStage.draw();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (gameStage != null) {
            gameStage.dispose();
        }

        if (gameOverStage != null) {
            gameOverStage.dispose();
        }
    }

    public void showGameOverStage(int currScore) {
        // 游戏结束舞台可见
        gameOverStage.setVisible(true);

        // 将输入处理设置到游戏结束舞台
        Gdx.input.setInputProcessor(gameOverStage);

        // 设置当前分数
        gameOverStage.setCurrentScore(currScore);
    }

    public void restartReadyGame() {
        gameOverStage.setVisible(false);

        // 将输入处理设置回主游戏舞台
        Gdx.input.setInputProcessor(gameStage);

        gameStage.ready();
    }
}
