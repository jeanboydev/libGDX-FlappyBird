package com.jeanboy.app.flappybird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class FPSDebug implements Disposable {

    private SpriteBatch batch;
    private BitmapFont bitmapFont;

    /**
     * 文本高度占屏幕高度的比例
     */
    private static final float OCCUPY_HEIGHT_RATIO = 14.0F / 480.0F;

    /**
     * 显示的文本偏移右下角的X轴和Y轴比例(相对于字体高度的比例)
     */
    private static final float DISPLAY_ORIGIN_OFFSET_RATIO = 0.5F;

    // 帧率字体绘制的位置
    private float x;
    private float y;

    public void init(BitmapFont fpsBitmapFont, int fontRawPixSize) {
        batch = new SpriteBatch();
        bitmapFont = fpsBitmapFont;

        float height = Gdx.graphics.getHeight();
        float scale = (height * OCCUPY_HEIGHT_RATIO) / (float) fontRawPixSize;
        bitmapFont.getData().setScale(scale);
        float scaleFontSize = fontRawPixSize * scale;
        float offset = scaleFontSize * DISPLAY_ORIGIN_OFFSET_RATIO;
        x = scaleFontSize - offset;
        y = scaleFontSize * 1.85f - offset;
    }

    public void render() {
        batch.begin();
        bitmapFont.draw(batch, "FPS:" + Gdx.graphics.getFramesPerSecond(), x, y);
        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
    }
}
