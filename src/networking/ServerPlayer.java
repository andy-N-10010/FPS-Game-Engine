package networking;

public class ServerPlayer {

    private final String username;

    private boolean moveUp, moveDown, moveLeft, MoveRight;

    private float x;
    private float y;

    public ServerPlayer(String username) {
        this.username = username;
    }

    public void update() {

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getUsername() {
        return username;
    }
}
