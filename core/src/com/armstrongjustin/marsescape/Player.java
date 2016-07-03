package com.armstrongjustin.marsescape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

import States.IdleState;
import States.State;

public class Player extends Sprite {

    public final String IDLE = "idle";
    public final String WALK = "walk";
    public final String JUMP = "jump";
    public final String CROUCH = "crouch";
    final String PLAYER_SHEET = "playerSheet.png";
    final String RIGHT = "right";
    final String LEFT = "left";

    final public int NORMAL_ACCELERATION = 20;
    final public float GROUND_DECELERATION = 10f;
    final public int MAX_XVELOCITY = 400;
    final public int INITIAL_JUMP_VELOCITY = 400;
    final public float GRAVITY = -15f;

    ArrayList<TextureRegion> animationFrames = new ArrayList<TextureRegion>();
    public HashMap<String, ArrayList<TextureRegion>> idleFrames = new HashMap<String, ArrayList<TextureRegion>>();
    public HashMap<String, ArrayList<TextureRegion>> walkFrames = new HashMap<String, ArrayList<TextureRegion>>();
    public HashMap<String, ArrayList<TextureRegion>> jumpframes = new HashMap<String, ArrayList<TextureRegion>>();
    public HashMap<String, ArrayList<TextureRegion>> crouchFrames = new HashMap<String, ArrayList<TextureRegion>>();
    HashMap<String, State> stateMap = new HashMap<String, State>();

    private TextureRegion currentFrame;
    public Boolean facingRight;
    public float xVelocity;
    public float yVelocity;
    private int frameIndex = 0;
    private long animationTimer = 0;
    public State state;
    private PlayerStateObserver stateManager;

    public Player (Vector2 startLocation) {
        stateManager = new PlayerStateObserver(this);
        setPosition(startLocation.x, startLocation.y);
        facingRight = true;
        makeAnimationFrames();
        state = new IdleState(this, idleFrames);
    }

    private void makeAnimationFrames(){
        Texture sheet = new Texture(PLAYER_SHEET);
        sheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        idleFrames = makeIdleFramesMap(sheet);
        walkFrames = makeWalkFramesMap(sheet);
        jumpframes = makeJumpFramesMap(sheet);
        crouchFrames = makeCrouchFramesMap(sheet);
    }


    private HashMap<String, ArrayList<TextureRegion>> makeIdleFramesMap(Texture sheet) {
        ArrayList<TextureRegion> rightIdleFrames = new ArrayList<TextureRegion>();
        TextureRegion idle0 = new TextureRegion(sheet, 0, 0, 32, 32);
        rightIdleFrames.add(idle0);

        ArrayList<TextureRegion> leftIdleFrames = makeFlippedFrameList(rightIdleFrames);
        HashMap<String, ArrayList<TextureRegion>> frameMap = new HashMap<String, ArrayList<TextureRegion>>();
        frameMap.put(RIGHT,rightIdleFrames);
        frameMap.put(LEFT, leftIdleFrames);

        return frameMap;
    }

    private HashMap<String, ArrayList<TextureRegion>> makeWalkFramesMap(Texture sheet) {
        TextureRegion walk0 = new TextureRegion(sheet, 0, 0, 32, 32);
        TextureRegion walk1 = new TextureRegion(sheet, 32, 0, 32, 32);
        TextureRegion walk2 = new TextureRegion(sheet, 64, 0, 32, 32);
        ArrayList<TextureRegion> rightWalkFrames = new ArrayList<TextureRegion>();
        rightWalkFrames.add(walk0);
        rightWalkFrames.add(walk1);
        rightWalkFrames.add(walk2);
        ArrayList<TextureRegion> leftWalkFrames = makeFlippedFrameList(rightWalkFrames);
        HashMap<String, ArrayList<TextureRegion>> frameMap = new HashMap<String, ArrayList<TextureRegion>>();
        frameMap.put(RIGHT, rightWalkFrames);
        frameMap.put(LEFT, leftWalkFrames);

        return frameMap;
    }

    private HashMap<String, ArrayList<TextureRegion>> makeJumpFramesMap(Texture sheet) {
        ArrayList<TextureRegion> rightJumpFrames = new ArrayList<TextureRegion>();
        TextureRegion jump0 = new TextureRegion(sheet, 32, 0, 32, 32);
        rightJumpFrames.add(jump0);
        ArrayList<TextureRegion> leftJumpFrames = makeFlippedFrameList(rightJumpFrames);
        HashMap<String, ArrayList<TextureRegion>> frameMap = new HashMap<String, ArrayList<TextureRegion>>();
        frameMap.put(RIGHT, rightJumpFrames);
        frameMap.put(LEFT, leftJumpFrames);

        return frameMap;
    }

    private HashMap<String, ArrayList<TextureRegion>> makeCrouchFramesMap(Texture sheet) {
        ArrayList<TextureRegion> rightCrouchFrames = new ArrayList<TextureRegion>();
        TextureRegion crouch0 = new TextureRegion(sheet, 96, 0, 32, 32);
        rightCrouchFrames.add(crouch0);
        ArrayList<TextureRegion> leftCrouchFrames = makeFlippedFrameList(rightCrouchFrames);

        HashMap<String, ArrayList<TextureRegion>> frameMap = new HashMap<String, ArrayList<TextureRegion>>();
        frameMap.put(RIGHT, rightCrouchFrames);
        frameMap.put(LEFT, leftCrouchFrames);

        return frameMap;
    }

    private ArrayList<TextureRegion> makeFlippedFrameList(ArrayList<TextureRegion> initialList) {
        ArrayList<TextureRegion> flippedList = new ArrayList<TextureRegion>();

        for (TextureRegion frame: initialList) {
            TextureRegion flippedFrame = new TextureRegion(frame);
            flippedFrame.flip(true, false);
            flippedList.add(flippedFrame);
        }

        return flippedList;
    }

    public void setXVelocity(float newXVelocity) {
        xVelocity = newXVelocity;
    }

    public void setYVelocity(float newYVelocity) {
        yVelocity = newYVelocity;
    }

    public void setAnimationDirection(HashMap<String, ArrayList<TextureRegion>> frameMap) {
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
        state.update();
        stateManager.update();
    }

    public void render(Batch batch, float dt) {

        float xPos = (float) Math.round(getX());
        float yPos = (float) Math.round(getY());

        batch.draw(currentFrame, xPos, yPos);
    }

    public String checkIfStopped() {
        if (xVelocity == 0) {
            return IDLE;
        }
        return state.name;
    }


    public void handleAnimation() {
        long currentTime = System.currentTimeMillis();
        long elaspsedTime = currentTime - animationTimer;
        int numberOfFrames = animationFrames.size();

        int timeBetweenFrames = 100;

        if (elaspsedTime >= timeBetweenFrames) {
            if (frameIndex >= (numberOfFrames - 1)) {
                frameIndex = 0;
            } else {
                frameIndex++;
            }

            animationTimer = currentTime;
            currentFrame = animationFrames.get(frameIndex);
        }
    }

    public void accelerateX (float acceleration) {
        xVelocity += acceleration;
    }

    public void accelerateY (float acceleration) {
        yVelocity += acceleration;
    }


    public void disposeTextures() {
    }
}
