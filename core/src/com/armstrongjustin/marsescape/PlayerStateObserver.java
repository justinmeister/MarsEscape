package com.armstrongjustin.marsescape;

import com.armstrongjustin.marsescape.Player;
import static com.armstrongjustin.marsescape.Constants.StateConstants.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Stack;

import States.CrouchState;
import States.IdleState;
import States.JumpState;
import States.State;
import States.WalkState;

public class PlayerStateObserver {
    Player player;
    ArrayList<String> newStateList = new ArrayList<String>();

    public PlayerStateObserver(Player player) {
        this.player = player;
    }

    public void onNotify(String stateName) {
        newStateList.add(stateName);
    }

    public void update() {
        if(!newStateList.isEmpty()) {
            String newState = newStateList.get(newStateList.size()-1);

            if (newState.equals(IDLE)) {
                player.state = new IdleState(player, player.idleFrames);
            }
            else if (newState.equals(WALK)) {
                player.state = new WalkState(player, player.walkFrames);
            }
            else if (newState.equals(JUMP)) {
                player.state = new JumpState(player, player.jumpframes);
            }
            else if (newState.equals(CROUCH)) {
                player.state = new CrouchState(player, player.crouchFrames);
            }

            newStateList = new ArrayList<String>();
        }
    }

}
