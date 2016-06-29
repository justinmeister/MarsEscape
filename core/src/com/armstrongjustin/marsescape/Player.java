package com.armstrongjustin.marsescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Sprite {

    final String IDLE = "idle";
    final String WALK = "walk";
    final String JUMP = "jump";
    final String CROUCH = "crouch";
    final String PLAYER_SHEET = "playerSheet.png";
    final String RIGHT = "right";
    final String LEFT = "left";
    final int NORMAL_ACCELERATION = 20;
    final float GROUND_DECELERATION = 10f;
    final int MAX_XVELOCITY = 400;
    final int INITIAL_JUMP_VELOCITY = 400;
    final float GRAVITY = 15f;

    ArrayList<TextureRegion> animationFrames = new ArrayList<TextureRegion>();
    HashMap<String, ArrayList<TextureRegion>> idleFrames = new HashMap<String, ArrayList<TextureRegion>>();
    HashMap<String, ArrayList<TextureRegion>> walkFrames = new HashMap<String, ArrayList<TextureRegion>>();
    HashMap<String, ArrayList<TextureRegion>> jumpframes = new HashMap<String, ArrayList<TextureRegion>>();
    HashMap<String, ArrayList<TextureRegion>> crouchFrames = new HashMap<String, ArrayList<TextureRegion>>();

    private TextureRegion currentFrame;
    private Boolean facingRight;
    public float xVelocity;
    public float yVelocity;
    private int frameIndex = 0;
    private long animationTimer = 0;
    public String state;

    public Player (Vector2 startLocation) {
        setPosition(startLocation.x, startLocation.y);
        facingRight = true;
        makeAnimationFrames();
        enterIdle();
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
        TextureRegion walk0 = new TextureRegion(sheet, 32, 0, 32, 32);
        TextureRegion walk1 = new TextureRegion(sheet, 64, 0, 32, 32);
        ArrayList<TextureRegion> rightWalkFrames = new ArrayList<TextureRegion>();
        rightWalkFrames.add(walk0);
        rightWalkFrames.add(walk1);
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


    private void enterIdle() {
        state = IDLE;
        xVelocity = 0;
        yVelocity = 0;
        setAnimationDirection(idleFrames);
    }

    private void enterWalk() {
        state = WALK;
        yVelocity = 0;
        setAnimationDirection(walkFrames);
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
        else if (state.equals(WALK)) {
            walking();
        }

        else if (state.equals(JUMP)) {
            jumping();
        }

        else if (state.equals(CROUCH)) {
            crouching();
        }

        updatePosition(dt);

    }

    private void updatePosition(float dt) {
        float newXPosition = (float) Math.round(getX() + xVelocity * dt);
        float newYPosition = (float) Math.round(getY() + yVelocity * dt);
        setX(newXPosition);
        setY(newYPosition);

        if (getY() < 32.0f) {
            setY(32.0f);
            enterWalk();

        }

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
            enterWalk();
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            facingRight = true;
            enterWalk();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            enterJump();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            enterCrouch();
        }
    }

    private void enterCrouch() {
        state = CROUCH;
        setAnimationDirection(crouchFrames);
    }

    private void enterJump() {
        state = JUMP;
        yVelocity = INITIAL_JUMP_VELOCITY;
        setAnimationDirection(jumpframes);
    }

    private void handleIdleAnimation() {
        int frameIndex = 0;
        currentFrame = animationFrames.get(frameIndex);
    }

    private void handleCrouchAnimation() {
        int frameIndex = 0;
        currentFrame = animationFrames.get(frameIndex);
    }

    private void walking () {
        handleWalkAnimation();
        checkInputDuringWalk();
        checkIfStopped();
    }

    private void checkIfStopped() {
        if (xVelocity == 0) {
            enterIdle();
        }
    }

    private void crouching () {
        handleCrouchAnimation();
        checkInputDuringCrouch();
        applyGroundFriction();
    }

    private void checkInputDuringCrouch() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (!facingRight) {
                facingRight = true;
                setAnimationDirection(crouchFrames);
            }
            enterWalk();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (facingRight) {
                facingRight = false;
                setAnimationDirection(crouchFrames);
            }
            enterWalk();
        }

        else if (!(Gdx.input.isKeyPressed(Input.Keys.DOWN))) {
            enterIdle();
        }

    }

    private void checkInputDuringWalk() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!facingRight) {
                facingRight = true;
                setAnimationDirection(walkFrames);
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
                setAnimationDirection(walkFrames);
            }

            if (xVelocity < MAX_XVELOCITY * -1) {
                xVelocity = MAX_XVELOCITY * -1;
            }
            else {
                xVelocity -= NORMAL_ACCELERATION;
            }
        }

        else {
            applyGroundFriction();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            enterJump();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            enterCrouch();
        }
    }

    private void applyGroundFriction() {
        if (facingRight) {
                if (xVelocity > 0) {
                    xVelocity -= GROUND_DECELERATION;
                }
                else {
                    xVelocity = 0;
                }
            }
            else {
                if (xVelocity < 0) {
                    xVelocity += GROUND_DECELERATION;
                }
                else {
                    xVelocity = 0;
                }
            }
    }

    private void handleWalkAnimation() {
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



    private void jumping() {
        handleJumpAnimation();
        yVelocity -= GRAVITY;
        checkInputDuringJump();
    }



    private void checkInputDuringJump() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!facingRight) {
                facingRight = true;
                setAnimationDirection(jumpframes);
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
                setAnimationDirection(jumpframes);
            }

            if (xVelocity < MAX_XVELOCITY * -1) {
                xVelocity = MAX_XVELOCITY * -1;
            }
            else {
                xVelocity -= NORMAL_ACCELERATION;
            }
        }
    }

    private void handleJumpAnimation() {
        int frameIndex = 0;
        currentFrame = animationFrames.get(frameIndex);

    }

    public void disposeTextures() {
    }
}
