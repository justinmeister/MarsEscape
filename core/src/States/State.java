package States;

import com.armstrongjustin.marsescape.Player;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class State {
    final String IDLE = "idle";
    final String WALK = "walk";
    final String JUMP = "jump";
    final String CROUCH = "crouch";
    public String name;
    HashMap<String, ArrayList<TextureRegion>> frameMap;

    public State (Player player, HashMap<String, ArrayList<TextureRegion>> frameMap) {
        this.frameMap = frameMap;
        enter(player);
    }

    protected abstract void enter(Player player);
    protected abstract String handleInput(Player player);
    public abstract String update(Player player);
}
