package States;

import com.armstrongjustin.marsescape.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;

public class JumpState extends State {

    public JumpState (Player player, HashMap<String, ArrayList<TextureRegion>> frameMap) {
        super(player, frameMap);
        name = JUMP;
    }

    @Override
    protected void enter() {
        player.setYVelocity(player.INITIAL_JUMP_VELOCITY);
        player.setAnimationDirection(frameMap);

    }

    @Override
    protected String handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!player.facingRight) {
                player.facingRight = true;
                player.setAnimationDirection(frameMap);
            }

            if (player.xVelocity > player.MAX_XVELOCITY) {
                player.setXVelocity(player.MAX_XVELOCITY);
            }
            else {
                player.accelerateX(player.NORMAL_ACCELERATION);
            }
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (player.facingRight) {
                player.facingRight = false;
                player.setAnimationDirection(frameMap);
            }

            if (player.xVelocity < player.MAX_XVELOCITY * -1) {
                player.setXVelocity(player.MAX_XVELOCITY * -1);
            }
            else {
                player.accelerateX(player.NORMAL_ACCELERATION);
            }
        }
        return name;
    }

    @Override
    public String update() {
        String newState;
        player.handleAnimation();
        player.accelerateY(player.GRAVITY);
        newState = handleInput();

        return newState;
    }
}
