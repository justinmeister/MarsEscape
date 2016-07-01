package States;

import com.armstrongjustin.marsescape.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;

public class WalkState extends State {

    public WalkState(Player player, HashMap<String, ArrayList<TextureRegion>> frameMap) {
        super(player, frameMap);
        name = WALK;
    }

    @Override
    protected void enter(Player player) {
        player.setAnimationDirection(frameMap);
        player.accelerateX(player.NORMAL_ACCELERATION);
    }

    @Override
    protected String handleInput(Player player) {
        float maxXVelocity = player.MAX_XVELOCITY;
        String newState = name;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!player.facingRight) {
                player.facingRight = true;
                player.setAnimationDirection(frameMap);
            }

            if (player.xVelocity > player.MAX_XVELOCITY) {
                player.setXVelocity(player.MAX_XVELOCITY);
            }
            else {
                player.setXVelocity(player.xVelocity += player.NORMAL_ACCELERATION);
            }
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (player.facingRight) {
                player.facingRight = false;
                player.setAnimationDirection(frameMap);
            }

            if (player.xVelocity < maxXVelocity * -1) {
                player.setXVelocity(maxXVelocity * -1);
            }
            else {
                player.setXVelocity(player.xVelocity -= player.NORMAL_ACCELERATION);
            }
        }

        else {
            player.applyGroundFriction();
            newState = player.checkIfStopped();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            newState = JUMP;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            newState = CROUCH;
        }



        return newState;
    }

    @Override
    public String update(Player player) {
        player.handleAnimation();
        return handleInput(player);
    }
}
