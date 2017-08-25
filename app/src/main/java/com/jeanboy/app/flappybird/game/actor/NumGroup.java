package com.jeanboy.app.flappybird.game.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jeanboy.app.flappybird.game.MainGame;
import com.jeanboy.app.flappybird.game.actor.base.BaseGroup;
import com.jeanboy.app.flappybird.game.actor.framework.ImageActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeanboy on 2017/8/25.
 */

public class NumGroup extends BaseGroup {

    private TextureRegion[] digitRegions;

    private final List<ImageActor> digitActorList = new ArrayList<>();

    private int num;

    public NumGroup(MainGame mainGame) {
        super(mainGame);
    }

    public NumGroup(MainGame mainGame, TextureRegion[] digitRegions) {
        super(mainGame);
        this.digitRegions = digitRegions;
    }

    public void setDigitRegions(TextureRegion[] digitRegions) {
        this.digitRegions = digitRegions;
        justNumDigit(num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        if (this.num != num && num >= 0) {
            this.num = num;
            justNumDigit(this.num);
        }
    }

    public void addNum(int numStep) {
        setNum(num + numStep);
    }

    private void justNumDigit(int num) {
        if (digitRegions == null) return;

        char[] numChars = ("" + num).toCharArray();

        if (digitActorList.size() > numChars.length) {
            int removeCount = digitActorList.size() - numChars.length;
            for (int i = 0; i < removeCount; i++) {
                removeActor(digitActorList.remove(0));
            }
        } else if (digitActorList.size() < numChars.length) {
            int addCount = numChars.length - digitActorList.size();
            for (int i = 0; i < addCount; i++) {
                ImageActor digitActor = new ImageActor();
                digitActorList.add(digitActor);
                addActor(digitActor);
            }
        }

        for (int i = 0; i < digitActorList.size(); i++) {
            TextureRegion region = digitRegions[Integer.parseInt("" + numChars[i])];
            digitActorList.get(i).setRegion(region);
        }

        justDigitLayout();
    }

    private void justDigitLayout() {
        float digitWidth = digitActorList.get(0).getWidth();
        float digitHeight = digitActorList.get(0).getHeight();
        for (int i = 0; i < digitActorList.size(); i++) {
            digitActorList.get(i).setX(digitWidth * i);
        }
        setSize(digitWidth * digitActorList.size(), digitHeight);
    }

}
