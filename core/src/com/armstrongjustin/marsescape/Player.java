package com.armstrongjustin.marsescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

    final String IDLE = "idle";
    final String RUN = "run";
    final String JUMP = "jump";
    final String PLAYER_SHEET = "playerSheet.png";

    public String state;
    Texture sheet;
    TextureRegion[] idleFrames;
    TextureRegion[] runFrames;

    Animation idleAnimation;
    Animation runAnimation;
    public Animation animation;

    Boolean facingRight;

    Float elapsedTime = 0f;

    public Player (Vector2 startLocation) {
        setPosition(startLocation.x, startLocation.y);
        state = IDLE;
        facingRight = true;

        makeAnimations();
        enterIdle();

    }

    private void makeAnimations(){
        sheet = new Texture(PLAYER_SHEET);

        TextureRegion frame1 = new TextureRegion(sheet, 0, 0, 32, 32);
        TextureRegion frame2 = new TextureRegion(sheet, 32, 0, 32, 32);
        TextureRegion frame3 = new TextureRegion(sheet, 64, 0, 32, 32);
        idleFrames = new TextureRegion[1];
        idleFrames[0] = frame1;

        runFrames = new TextureRegion[2];
        runFrames[0] = frame2;
        runFrames[1] = frame3;

        idleAnimation = new Animation(1f/15f, idleFrames);
        runAnimation = new Animation(1f/10f, runFrames);

    }

    private void enterIdle() {
        state = IDLE;
        animation = idleAnimation;
    }

    private void enterRun() {
        state = RUN;
        animation = runAnimation;
    }

    public void update(float dt) {
        if (state.equals(IDLE)) {
            idling();
        }
        else if (state.equals(RUN)) {
            running();
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
