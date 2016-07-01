package States;

import com.armstrongjustin.marsescape.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;

public class CrouchState extends State {

    public CrouchState(Player player, HashMap<String, ArrayList<TextureRegion>> frameMap) {
        super(player, frameMap);
        name = CROUCH;
    }

    @Override
    protected void enter(Player player) {
        player.setAnimationDirection(frameMap);

    }

    @Override
    protected String handleInput(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (!player.facingRight) {
                player.facingRight = true;
            }
            return WALK;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (player.facingRight) {
                player.facingRight = false;
            }
            return WALK;
        }

        else if (!(Gdx.input.isKeyPressed(Input.Keys.DOWN))) {
            return IDLE;
        }

        player.applyGroundFriction();

        return name;
    }

    @Override
    public String update(Player player) {
        player.handleAnimation();
        return handleInput(player);
    }
}
