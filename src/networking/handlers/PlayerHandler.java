package networking.handlers;


import networking.ServerPlayer;

import java.util.LinkedList;

public class PlayerHandler {

    public static final PlayerHandler Instance = new PlayerHandler();

    private final LinkedList<ServerPlayer> players;

    public PlayerHandler() {
        this.players = new LinkedList<>();
    }

    public void update() {
        for (int i = 0; i < this.players.size(); i++) {
            this.players.get(i).update();
        }

    }

}
