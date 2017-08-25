package com.jeanboy.app.flappybird.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jeanboy.app.flappybird.game.MainGame;
import com.jeanboy.app.flappybird.game.Res;
import com.jeanboy.app.flappybird.game.actor.base.BaseImageActor;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class FloorActor extends BaseImageActor {

    private float moveVelocity;
    private TextureRegion region;
    private float offsetX;
    private boolean isMove;

    public FloorActor(MainGame mainGame) {
        super(mainGame);
        region = getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_FLOOR);
        setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
    }

    public float getMoveVelocity() {
        return moveVelocity;
    }

    public void setMoveVelocity(float moveVelocity) {
        this.moveVelocity = moveVelocity;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    @Override
    public void act(float delta) {
//        super.act(delta);
        if (isMove) {
            offsetX += (delta * moveVelocity);//根据时间与速度计算出偏移量
            offsetX %= getWidth();//最大为屏幕宽度
            if (offsetX > 0) {
                offsetX -= getWidth();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
        batch.draw(region, getX() + offsetX, getY());
        if (Math.abs(offsetX) > 0.001f) {
            batch.draw(region, getX() + (getWidth() + offsetX), getY());
        }
    }
}
