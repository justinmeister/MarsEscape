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
    final int NORMAL_ACCELERATION = 20;
    final float GROUND_DECELERATION = 10f;
    final int MAX_XVELOCITY = 400;

    public String state;
    Texture sheet;
    ArrayList<TextureRegion> animationFrames = new ArrayList<TextureRegion>();

    ArrayList<TextureRegion> rightIdleFrames = new ArrayList<TextureRegion>();
    ArrayList<TextureRegion> leftIdleFrames = new ArrayList<TextureRegion>();
    HashMap<String, ArrayList<TextureRegion>> idleFrames = new HashMap<String, ArrayList<TextureRegion>>();


    ArrayList<TextureRegion> leftWalkFrames = new ArrayList<TextureRegion>();
    ArrayList<TextureRegion> rightWalkFrames = new ArrayList<TextureRegion>();
    HashMap<String, ArrayList<TextureRegion>> walkingFrames = new HashMap<String, ArrayList<TextureRegion>>();

    TextureRegion currentFrame;
    Boolean facingRight;

    public float xVelocity;

    private int frameIndex = 0;

    private long animationTimer = 0;

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
        animationTimer = System.currentTimeMillis();
        xVelocity = 0;
        state = IDLE;
        setAnimationDirection(idleFrames);
    }

    private void enterRun() {
        animationTimer = System.currentTimeMillis();
        state = RUN;
        setAnimationDirection(walkingFrames);
    }

    private void setAnimationDirection(HashMap<String, ArrayList<TextureRegion>> frameMap) {
        frameIndex = 0;
        animationTimer = System.currentTimeMillis();

        if (facingRight) {
            animationFrames = frameMap.get(RIGHT);
        }
        else {
            animationFrames = frameMap.get(LEFT);
        }

        currentFrame = animationFrames.get(frameIndex);
    }


    public void update(float dt) {
        if (state.equals(IDLE)) {
            idling();
        }
        else if (state.equals(RUN)) {
            running(dt);
        }

        else if (state.equals(JUMP)) {
            jumping();
        }

        updatePosition(dt);

    }

    private void updatePosition(float dt) {
        float newXPosition = (float) Math.round(getX() + xVelocity * dt);
        setX(newXPosition);

    }


    public void render(Batch batch, float dt) {

        float xPos = (float) Math.round(getX());
        float yPos = (float) Math.round(getY());

        batch.draw(currentFrame, xPos, yPos);
    }

    private void idling() {
        handleIdleAnimation();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            facingRight = false;
            enterRun();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            facingRight = true;
            enterRun();
        }
    }

    private void handleIdleAnimation() {
        int frameIndex = 0;
        currentFrame = animationFrames.get(frameIndex);

    }

    private void running(float dt) {
        handleRunAnimation();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!facingRight) {
                facingRight = true;
                setAnimationDirection(walkingFrames);
            }

            if (xVelocity > MAX_XVELOCITY) {
                xVelocity = MAX_XVELOCITY;
            }
            else {
                xVelocity += NORMAL_ACCELERATION;

            }

        }

        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (facingRight) {
                facingRight = false;
                setAnimationDirection(walkingFrames);
            }

            if (xVelocity < MAX_XVELOCITY * -1) {
                xVelocity = MAX_XVELOCITY * -1;
            }
            else {
                xVelocity -= NORMAL_ACCELERATION;
            }

        }

        else {
            if (facingRight) {
                if (xVelocity > 0) {
                    xVelocity -= GROUND_DECELERATION;
                }
                else {
                    enterIdle();
                }
            }
            else {
                if (xVelocity < 0) {
                    xVelocity += GROUND_DECELERATION;
                }
                else {
                    enterIdle();
                }
            }
        }



        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            enterIdle();
        }

    }

    private void handleRunAnimation() {
        long currentTime = System.currentTimeMillis();
        long elaspsedTime = currentTime - animationTimer;
        int numberOfFrames = animationFrames.size();

        int timeBetweenFrames = 100;

        if (elaspsedTime >= timeBetweenFrames) {
            if (frameIndex >= (numberOfFrames-1)) {
                frameIndex = 0;
            }
            else {
                frameIndex++;
            }

            animationTimer = currentTime;
            currentFrame = animationFrames.get(frameIndex);
        }


    }





    private void jumping() {}

    public void disposeTextures() {
        sheet.dispose();
    }
}
