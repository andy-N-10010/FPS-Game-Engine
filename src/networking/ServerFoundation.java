package networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import networking.global.PlayerAddEvent;
import networking.global.PlayerRemoveEvent;
import networking.global.PlayerUpdateEvent;
import networking.screens.ConnectionScreen;

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

//        this.server.getKryo().register(PlayerAddEvent.class);
//        this.server.getKryo().register(PlayerRemoveEvent.class);
//        this.server.getKryo().register(PlayerUpdateEvent.class);
//        this.server.getKryo().register(String.class);

//        Kryo kryo = server.getKryo();
//        kryo.register(SomeRequest.class);
//        kryo.register(SomeResponse.class);
//        Kryo kryo = client.getKryo();
//        kryo.register(SomeRequest.class);
//        kryo.register(SomeResponse.class);

        //this.server.getKryo().register(ConnectionScreen.SomeRequest.class);


        this.bindServer(6334, 6334);

        this.server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SomeRequest) {
                    SomeRequest request = (SomeRequest)object;
                    System.out.println(request.text);

                    SomeResponse response = new SomeResponse();
                    response.text = "Thanks";
                    connection.sendTCP(response);
                }
            }
        });


    }

    public class SomeRequest {
        public String text;
    }
    public class SomeResponse {
        public String text;
    }

    public void bindServer(final int tcpPort, final int udpPort) {
        this.server.start();

        try {

            this.server.bind(tcpPort, udpPort);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    
}
