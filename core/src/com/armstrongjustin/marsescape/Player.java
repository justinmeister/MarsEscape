package com.armstrongjustin.marsescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Player extends Sprite {

    final String IDLE = "idle";
    final String RUN = "run";
    final String JUMP = "jump";
    final String PLAYER_SHEET = "playerSheet.png";
    final String RIGHT = "right";
    final String LEFT = "left";

    public String state;
    Texture sheet;
    ArrayList<TextureRegion> animationFrames = new ArrayList<TextureRegion>();

    ArrayList<TextureRegion> rightIdleFrames = new ArrayList<TextureRegion>();
    ArrayList<TextureRegion> leftIdleFrames = new ArrayList<TextureRegion>();
    HashMap<String, ArrayList<TextureRegion>> idleFrames = new HashMap<String, ArrayList<TextureRegion>>();


    ArrayList<TextureRegion> leftWalkFrames = new ArrayList<TextureRegion>();
    ArrayList<TextureRegion> rightWalkFrames = new ArrayList<TextureRegion>();
    HashMap<String, ArrayList<TextureRegion>> walkingFrames = new HashMap<String, ArrayList<TextureRegion>>();


    Boolean facingRight;

    Float elapsedTime = 0f;

    public Player (Vector2 startLocation) {
        setPosition(startLocation.x, startLocation.y);
        state = IDLE;
        facingRight = true;

        makeAnimationFrames();
        enterIdle();

    }

    private void makeAnimationFrames(){
        sheet = new Texture(PLAYER_SHEET);
        sheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        TextureRegion idle0 = new TextureRegion(sheet, 0, 0, 32, 32);
        rightIdleFrames.add(idle0);
        makeFlippedFrameList(rightIdleFrames, leftIdleFrames);
        idleFrames.put(RIGHT,rightIdleFrames);
        idleFrames.put(LEFT, leftIdleFrames);

        TextureRegion walk0 = new TextureRegion(sheet, 32, 0, 32, 32);
        TextureRegion walk1 = new TextureRegion(sheet, 64, 0, 32, 32);
        rightWalkFrames.add(walk0);
        rightWalkFrames.add(walk1);
        makeFlippedFrameList(rightWalkFrames, leftWalkFrames);
        walkingFrames.put(RIGHT, rightWalkFrames);
        walkingFrames.put(LEFT, leftWalkFrames);

    }



    private void makeFlippedFrameList(ArrayList<TextureRegion> initialList, ArrayList<TextureRegion> flippedList) {
        for (TextureRegion frame: initialList) {
            TextureRegion flippedFrame = new TextureRegion(frame);
            flippedFrame.flip(true, false);
            flippedList.add(flippedFrame);
        }
    }


    private void enterIdle() {
        state = IDLE;
        setAnimationFrames(idleFrames);
    }

    private void enterRun() {
        state = RUN;
        setAnimationFrames(walkingFrames);
    }

    private void setAnimationFrames(HashMap<String, ArrayList<TextureRegion>> frameMap) {
        if (facingRight) {
            animationFrames = frameMap.get(RIGHT);
        }
        else {
            animationFrames = frameMap.get(LEFT);
        }

    }
    }

    public void update(float dt) {
        if (state.equals(IDLE)) {
            idling();
        }
        else if (state.equals(RUN)) {
            running();
            translateX(100.0f * dt);
        }

        else if (state.equals(JUMP)) {
            jumping();
        }

    }

    public void render(Batch batch, float dt) {
        elapsedTime += dt;

        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);


        batch.draw(currentFrame, getX(), getY());
    }

    private void idling() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            facingRight = false;
            flipFrameCheck();
            enterRun();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            facingRight = true;
            flipFrameCheck();
            enterRun();
        }
    }

    private void flipFrameCheck(){
        for (TextureRegion region : animation.getKeyFrames()) {
            if (facingRight) {
                if (region.isFlipX()) {
                    region.flip(true, false);
                }
            }
            else {
                if (!region.isFlipX()) {
                    region.flip(true, false);
                }
            }

        }
    }


    private void running() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            facingRight = true;
            flipFrameCheck();

        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            facingRight = false;
            flipFrameCheck();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            enterIdle();
            flipFrameCheck();
        }
    }

    private void jumping() {}

    public void disposeTextures() {
        sheet.dispose();
    }
}
