package networking;


import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class PositionServer {
    private Server server;

    public PositionServer () throws IOException {
        server = new Server();
        Network.register(server);



        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {

                if (object instanceof Network.SomeRequest) {

                    Network.SomeRequest request = (Network.SomeRequest)object;
                    System.out.println(request.text);

                    Network.SomeResponse response = new Network.SomeResponse();
                    response.text = "Thanks";
                    connection.sendTCP(response);
                }
            }
        });


        server.start();

        server.bind(Network.port);


    }

    public static void main (String[] args) throws IOException {
        //Log.set(Log.LEVEL_DEBUG);
        new PositionServer();
    }

}
