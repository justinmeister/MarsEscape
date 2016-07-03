package States;
import com.armstrongjustin.marsescape.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;

public class IdleState extends State {

    public IdleState(Player player, HashMap<String, ArrayList<TextureRegion>> frameMap) {
        super(player, frameMap);
        name = IDLE;
    }

    @Override
    protected void enter() {
        player.setXVelocity(0);
        player.setYVelocity(0);
        player.setAnimationDirection(frameMap);
    }

    @Override
    public String update() {
        player.handleAnimation();
        return handleInput();

    }

    @Override
    protected String handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.facingRight = false;
            return WALK;
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.facingRight = true;
            return WALK;
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            return JUMP;
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            return CROUCH;
        }

        return name;
    }
}
