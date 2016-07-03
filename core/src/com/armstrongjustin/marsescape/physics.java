package com.armstrongjustin.marsescape;

public class Physics {

    Player player;

    public Physics(Player player) {
        this.player = player;
    }

    public void update(float dt) {
        movePlayerX(dt);
        movePlayerY(dt);
    }

    private void movePlayerX(float dt) {
        float newXPosition = (float) Math.round(player.getY() + player.yVelocity * dt);
        checkForFriction();
        player.setX(newXPosition);
    }

    private void movePlayerY(float dt) {
        float newYPosition = (float) Math.round(player.getX() + player.xVelocity * dt);
        player.setY(newYPosition);
    }

    private void checkForFriction() {
        if (player.state.name.equals(player.CROUCH) || player.state.name.equals(player.WALK)) {
            applyGroundFriction();
        }
    }

    public void applyGroundFriction() {
        if (player.facingRight) {
            if (player.xVelocity > 0) {
                player.xVelocity -= player.GROUND_DECELERATION;
            }
            else {
                player.xVelocity = 0;
            }
        }
        else {
            if (player.xVelocity < 0) {
                player.xVelocity += player.GROUND_DECELERATION;
            }
            else {
                player.xVelocity = 0;
            }
        }
    }
}
