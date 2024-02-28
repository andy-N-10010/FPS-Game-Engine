package networking;

import com.esotericsoftware.kryonet.Server;
import networking.global.PlayerAddEvent;
import networking.global.PlayerRemoveEvent;
import networking.global.PlayerUpdateEvent;

import java.io.IOException;
//import gdx.lunar.server.netty.NettyServer;

public class ServerFoundation {

    public static ServerFoundation instance;

    private Server server;

    public static void main(String[] args) {

        //Port
        instance = new ServerFoundation();
    }

    public ServerFoundation() {
        this.server = new Server(1_000_000, 1_000_000);

        this.server.getKryo().register(PlayerAddEvent.class);
        this.server.getKryo().register(PlayerRemoveEvent.class);
        this.server.getKryo().register(PlayerUpdateEvent.class);
        this.server.getKryo().register(String.class);


        this.bindServer(6334,6334);


    }

    public void bindServer(final int tcpPort, final int udpPort) {
        this.server.start();

        try {

            this.server.bind(tcpPort, udpPort);

        } catch(IOException e) {
            System.err.println("Error binding server");
        }
    }


    
}
