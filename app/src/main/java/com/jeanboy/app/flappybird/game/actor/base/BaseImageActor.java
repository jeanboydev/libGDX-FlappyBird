package com.jeanboy.app.flappybird.game.actor.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jeanboy.app.flappybird.game.MainGame;
import com.jeanboy.app.flappybird.game.actor.framework.ImageActor;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class BaseImageActor extends ImageActor {

    private MainGame mainGame;

    public BaseImageActor(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public BaseImageActor(MainGame mainGame, TextureRegion region) {
        super(region);
        this.mainGame = mainGame;
    }

    public MainGame getMainGame() {
        return mainGame;
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
    }
}
