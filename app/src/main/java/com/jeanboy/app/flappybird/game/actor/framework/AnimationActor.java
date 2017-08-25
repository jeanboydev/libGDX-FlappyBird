package com.jeanboy.app.flappybird.game.actor.framework;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by jeanboy on 2017/8/25.
 */

public class AnimationActor extends ImageActor {

    private Animation animation;

    private boolean isPlayAnimation = true;

    private int showFrameIndex;

    private float stateTime;

    public AnimationActor() {
    }

    public AnimationActor(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        this(new Animation(frameDuration, keyFrames));
    }

    public AnimationActor(float frameDuration, TextureRegion... keyFrames) {
        this(new Animation(frameDuration, keyFrames));
    }

    public AnimationActor(Animation animation) {
        setAnimation(animation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation != null) {
            TextureRegion region = null;
            if (isPlayAnimation) {
                stateTime += delta;
                region = animation.getKeyFrame(stateTime);
            } else {
                TextureRegion[] frameArray = animation.getKeyFrames();
                if (showFrameIndex >= 0 && showFrameIndex < frameArray.length) {
                    region = frameArray[showFrameIndex];
                }
            }
            setRegion(region);
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
        //默认显示第一帧
        if (this.animation != null) {
            TextureRegion[] frameArray = this.animation.getKeyFrames();
            if (frameArray.length > 0) {
                setRegion(frameArray[0]);
            }
        }
    }

    public boolean isPlayAnimation() {
        return isPlayAnimation;
    }

    public void setPlayAnimation(boolean playAnimation) {
        isPlayAnimation = playAnimation;
    }

    public int getShowFrameIndex() {
        return showFrameIndex;
    }

    public void setShowFrameIndex(int showFrameIndex) {
        this.showFrameIndex = showFrameIndex;
    }
}
