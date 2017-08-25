package com.jeanboy.app.flappybird.game.actor;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.jeanboy.app.flappybird.game.MainGame;
import com.jeanboy.app.flappybird.game.Res;
import com.jeanboy.app.flappybird.game.actor.base.BaseAnimationActor;
import com.jeanboy.app.flappybird.game.config.GameState;

/**
 * Created by jeanboy on 2017/8/25.
 */

public class BirdActor extends BaseAnimationActor {

    private GameState gameState;

    private float velocityY;//小鸟竖直方向上的速度

    private float gravity = Res.Physics.GRAVITY;//重力加速度

    private final static float FRAME_DURATION_DEFAULT = 0.2f;
    private final static float FRAME_DURATION_FLYING = 0.18f;

    public BirdActor(MainGame mainGame) {
        super(mainGame);

        Animation animation = new Animation(FRAME_DURATION_DEFAULT, getMainGame().getAtlas().findRegions(Res.Atlas
                .IMAGE_BIRD_YELLOW_01_TO_03));
        animation.setPlayMode(Animation.PlayMode.LOOP);//循环播放
        setAnimation(animation);

        refreshFrameAndRotation(GameState.ready);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        //在 飞翔状态 或 撞到水管后掉落到地板前 才应用物理效应
        if (gameState == GameState.fly || gameState == GameState.die) {
            velocityY += gravity * delta;
            setY(getY() + velocityY * delta);
        }

        if (gameState == GameState.fly) {
            changeBirdRotation(delta);
        }
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void refreshFrameAndRotation(GameState gameState) {
        if (gameState == null || this.gameState == gameState) return;
        this.gameState = gameState;
        switch (this.gameState) {
            case ready:
                setPlayAnimation(true);
                setRotation(0);
                getAnimation().setFrameDuration(FRAME_DURATION_DEFAULT);
                break;
            case fly:
                setPlayAnimation(true);
                getAnimation().setFrameDuration(FRAME_DURATION_FLYING);
                break;
            case die:
                break;
            case gameOver:
                setPlayAnimation(false);
                setShowFrameIndex(1);
                setRotation(-90);
                break;
        }
    }

    private void changeBirdRotation(float delta) {
        float rotation = getRotation();
        rotation += (velocityY * delta);

        if (velocityY > 0) {
            // 向上飞时稍微加大角度旋转的速度
            rotation += (velocityY * delta) * 1.5F;
        } else {
            // 向下飞时稍微减小角度旋转的速度
            rotation += (velocityY * delta) * 0.2F;
        }

        // 校准旋转角度: -75 <= rotation <= 45
        if (rotation < -75) {
            rotation = -75;
        } else if (rotation > 45) {
            rotation = 45;
        }

        // 设置小鸟的旋转角度
        setRotation(rotation);
    }
}
