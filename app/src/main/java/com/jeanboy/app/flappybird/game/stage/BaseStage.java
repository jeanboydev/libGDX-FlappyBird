package com.jeanboy.app.flappybird.game.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jeanboy.app.flappybird.game.MainGame;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class BaseStage extends Stage {

    private MainGame mainGame;

    private boolean isVisible = true;

    public BaseStage(MainGame mainGame,Viewport viewport) {
        super(viewport);
        this.mainGame = mainGame;
    }

    public MainGame getMainGame() {
        return mainGame;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
