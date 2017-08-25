package com.jeanboy.app.flappybird.game.actor;

import com.badlogic.gdx.utils.Pool;
import com.jeanboy.app.flappybird.game.MainGame;
import com.jeanboy.app.flappybird.game.Res;
import com.jeanboy.app.flappybird.game.actor.base.BaseImageActor;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class PipesActor extends BaseImageActor implements Pool.Poolable {

    private float moveVelocity;

    private boolean isUp;

    private boolean isPassByBird;

    private boolean isMove;

    public PipesActor() {
        super(null);
    }

    public PipesActor(MainGame mainGame) {
        super(mainGame);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isMove) {
            setX(getX() + moveVelocity * delta);
        }
    }

    public float getMoveVelocity() {
        return moveVelocity;
    }

    public void setMoveVelocity(float moveVelocity) {
        this.moveVelocity = moveVelocity;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
        if (this.isUp) {
            setRegion(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_BAR_UP));
        } else {
            setRegion(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_BAR_DOWN));
        }
    }

    public boolean isPassByBird() {
        return isPassByBird;
    }

    public void setPassByBird(boolean passByBird) {
        isPassByBird = passByBird;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    @Override
    public void reset() {
        setMove(false);
        setPassByBird(false);
    }
}
