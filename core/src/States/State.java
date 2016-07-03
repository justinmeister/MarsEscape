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
    Player player;


    public State (Player player, HashMap<String, ArrayList<TextureRegion>> frameMap) {
        this.frameMap = frameMap;
        this.player = player;
        enter();
    }

    protected abstract void enter();
    protected abstract String handleInput();
    public abstract String update();
}
