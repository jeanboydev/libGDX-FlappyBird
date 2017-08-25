package com.jeanboy.app.flappybird.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jeanboy.app.flappybird.game.MainGame;
import com.jeanboy.app.flappybird.game.Res;
import com.jeanboy.app.flappybird.game.actor.BigScoreActor;
import com.jeanboy.app.flappybird.game.actor.BirdActor;
import com.jeanboy.app.flappybird.game.actor.FloorActor;
import com.jeanboy.app.flappybird.game.actor.PipesActor;
import com.jeanboy.app.flappybird.game.actor.framework.ImageActor;
import com.jeanboy.app.flappybird.game.config.GameState;
import com.jeanboy.app.flappybird.game.utils.CollisionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jeanboy on 2017/8/24.
 */

public class GameStage extends BaseStage {

    private ImageActor bgActor;//背景

    private FloorActor floorActor;//地板

    private final List<PipesActor> pipesActorList = new ArrayList<>();
    private Pool<PipesActor> pipesActorPool;

    private float minDownPipesTopY;
    private float maxDownPipesTopY;
    private float generatePipesTimeCounter;

    private BirdActor birdActor;
    private float birdStartPositionY;

    private GameState gameState;

    private ImageActor tapTipsActor;//点击提示
    private ImageActor getReadyActor;//准备状态提示

    private BigScoreActor bigScoreActor;//大数字分数显示

    private Sound hitSound;
    private Sound scoreSound;
    private Sound touchSound;
    private Sound dieSound;


    public GameStage(MainGame mainGame, Viewport viewport) {
        super(mainGame, viewport);
        init();
    }

    private void init() {
        //背景
        bgActor = new ImageActor(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_BG));
        bgActor.setCenter(getWidth() / 2, getHeight() / 2);
        addActor(bgActor);

        //地板
        floorActor = new FloorActor(getMainGame());
        floorActor.setMoveVelocity(Res.Physics.MOVE_VELOCITY);
        addActor(floorActor);

        //水管
        pipesActorPool = Pools.get(PipesActor.class, 10);

        float pipesHeight = 400;//水管高度
        float maxRegion = 300;//水管变化幅度
        //水管最低高度为 高出地板40
        minDownPipesTopY = Math.max(floorActor.getTopY() + 40, getHeight() - pipesHeight - Res.Physics.PIPES_INTERVAL);
        //最低水管高度+300，屏幕高度-空白高度-60
        maxDownPipesTopY = Math.min(minDownPipesTopY + maxRegion, getHeight() - Res.Physics.PIPES_INTERVAL - 60);
        maxDownPipesTopY = Math.min(maxDownPipesTopY, floorActor.getTopY() + pipesHeight);


        //准备提示
        getReadyActor = new ImageActor(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_READY));
        getReadyActor.setCenterX(getWidth() / 2);
        getReadyActor.setTopY(getHeight() - 182);
        addActor(getReadyActor);

        //点击提示
        tapTipsActor = new ImageActor(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_TAP_TIP));
        tapTipsActor.setCenterX(getWidth() / 2);
        tapTipsActor.setCenterY((getReadyActor.getY() + floorActor.getTopY()) / 2);
        addActor(tapTipsActor);

        //小鸟
        birdActor = new BirdActor(getMainGame());
        birdActor.setX(tapTipsActor.getX() - 20);
        birdActor.setY(tapTipsActor.getY() + 80);
        birdStartPositionY = birdActor.getY();
        birdActor.setScale(1.2f);
        birdActor.setOrigin(Align.center);
        addActor(birdActor);

        floorActor.setZIndex(birdActor.getZIndex());

        //大数字分数显示
        bigScoreActor = new BigScoreActor(getMainGame());
        bigScoreActor.setCenterX(getWidth() / 2);
        bigScoreActor.setTopY(getHeight() - 50);
        addActor(bigScoreActor);
        bigScoreActor.setZIndex(getRoot().getChildren().size - 1);

        //获取音效
        hitSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_HIT, Sound.class);
        scoreSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_SCORE, Sound.class);
        touchSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_TOUCH, Sound.class);
        dieSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_DIE, Sound.class);

        ready();
    }

    public void ready() {
        gameState = GameState.ready;

        birdActor.setY(birdStartPositionY);
        birdActor.refreshFrameAndRotation(gameState);

        floorActor.setMove(false);

        for (PipesActor pipesActor : pipesActorList) {
            getRoot().removeActor(pipesActor);
        }

        pipesActorList.clear();

        tapTipsActor.setVisible(true);
        getReadyActor.setVisible(true);

        // 分数清零
        bigScoreActor.setNum(0);
        // 更新分数后重新水平居中
        bigScoreActor.setCenterX(getWidth() / 2);
    }


    private void startGame() {
        gameState = GameState.fly;

        birdActor.refreshFrameAndRotation(gameState);

        floorActor.setMove(true);

        tapTipsActor.setVisible(false);
        getReadyActor.setVisible(false);

        generatePipesTimeCounter = 0.0F;
    }

    private void gameOver() {
        gameState = GameState.gameOver;

        birdActor.refreshFrameAndRotation(gameState);

        floorActor.setMove(false);

        for (PipesActor pipesActor : pipesActorList) {
            if (pipesActor.isMove()) {
                pipesActor.setMove(false);
            }
        }

        getMainGame().getGameScreen().showGameOverStage(bigScoreActor.getNum());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (gameState == GameState.fly || gameState == GameState.die) {
            checkLogic();
        }

        if (gameState == GameState.fly) {//小鸟飞翔时才绘制水管
            generatePipesTimeCounter += delta;
            if (generatePipesTimeCounter >= Res.Physics.GENERATE_PIPES_TIME_INTERVAL) {
                generatePipes();
                generatePipesTimeCounter = 0;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        pipesActorList.clear();
    }

    private void checkLogic() {
        if (gameState == GameState.fly) {
            for (PipesActor pipesActor : pipesActorList) {
                // 是否碰撞到水管
                if (CollisionUtils.isCollision(birdActor, pipesActor, Res.Physics.DEPTH)) {
                    Gdx.app.log(MainGame.TAG, "碰撞到水管！");
                    collisionBar();
                    hitSound.play();
                    break;
                }
                //分数处理
                if (!pipesActor.isPassByBird() && pipesActor.isUp() && birdActor.getX() > pipesActor.getRightX()) {
                    bigScoreActor.addNum(1);
                    bigScoreActor.setCenterX(getWidth() / 2);
                    scoreSound.play();
                    pipesActor.setPassByBird(true);
                    Gdx.app.log(MainGame.TAG, "Score: " + bigScoreActor.getNum());
                }
            }
        }

        //移除屏幕外的水管
        Iterator<PipesActor> iterator = pipesActorList.iterator();
        PipesActor pipesActor = null;
        while (iterator.hasNext()) {
            pipesActor = iterator.next();
            if (pipesActor.getRightX() < 0) {
                getRoot().removeActor(pipesActor);
                iterator.remove();
                pipesActorPool.free(pipesActor);
            }
        }

        if (CollisionUtils.isCollision(birdActor, floorActor, Res.Physics.DEPTH)) {
            Gdx.app.log(MainGame.TAG, "碰撞到地板.");
            gameOver();
            // 播放游戏结束音效
            dieSound.play();
        }
    }

    private void collisionBar() {
        gameState = GameState.die;

        // 所有水管停止移动
        for (PipesActor pipesActor : pipesActorList) {
            if (pipesActor.isMove()) {
                pipesActor.setMove(false);
            }
        }

        // 地板停止移动
        floorActor.setMove(false);
    }

    private void generatePipes() {
        float downPipesTopY = MathUtils.random(minDownPipesTopY, maxDownPipesTopY);

        //创建底部水管
        PipesActor downPipesActor = pipesActorPool.obtain();
        downPipesActor.setMainGame(getMainGame());
        downPipesActor.setUp(false);
        downPipesActor.setX(getWidth());
        downPipesActor.setTopY(downPipesTopY);
        downPipesActor.setMoveVelocity(Res.Physics.MOVE_VELOCITY);
        downPipesActor.setMove(true);
        addActor(downPipesActor);
        pipesActorList.add(downPipesActor);
        // 将水管设置到小鸟后面(必须在 actor 添加到 stage 后设置 ZIndex 才有效)
        downPipesActor.setZIndex(floorActor.getZIndex());

        //创建顶部水管
        PipesActor upPipesActor = pipesActorPool.obtain();
        upPipesActor.setMainGame(getMainGame());
        upPipesActor.setUp(true);
        upPipesActor.setX(getWidth());
        upPipesActor.setY(downPipesActor.getTopY() + Res.Physics.PIPES_INTERVAL);
        upPipesActor.setMoveVelocity(Res.Physics.MOVE_VELOCITY);
        upPipesActor.setMove(true);
        addActor(upPipesActor);
        pipesActorList.add(upPipesActor);
        upPipesActor.setZIndex(floorActor.getZIndex());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (gameState == GameState.ready) {
            startGame();
            touchSound.play();
            birdActor.setVelocityY(Res.Physics.JUMP_VELOCITY);//给小鸟一个向上的速度
        } else if (gameState == GameState.fly) {
            if (birdActor.getTopY() < getHeight()) {
                birdActor.setVelocityY(Res.Physics.JUMP_VELOCITY);
                touchSound.play();
            }
        }
        return true;
    }
}
