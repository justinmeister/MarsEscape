package com.armstrongjustin.marsescape;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

    final String IDLE = "idle";
    final String RUN = "run";
    final String JUMP = "jump";

    public String state;

    public Player (Vector2 startLocation) {
        setPosition(startLocation.x, startLocation.y);
        state = IDLE;
    }

    public void update() {
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

    private void idling() {

    }

    private void running() {}

    private void jumping() {}
}
