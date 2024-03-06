package networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import networking.screens.ConnectionScreen;

import java.io.IOException;

public class PositionClient {
    Client client;
    String username;

    public PositionClient() {
        client = new Client();
        client.start();

        Network.register(client);


        try {
            client.connect(5000, "localhost", Network.port);
            // Server communication after connection can go here, or in Listener#connected().
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Network.SomeRequest request = new Network.SomeRequest();
        request.text = "Here is the request";
        client.sendTCP(request);

        client.addListener(new Listener() {
            public void connected (Connection connection) {
            }

            public void received (Connection connection, Object object) {
                    if (object instanceof Network.SomeResponse) {
                        Network.SomeResponse response = (Network.SomeResponse)object;
                        System.out.println(response.text);
                    }

            }

            public void disconnected (Connection connection) {
                System.exit(0);
            }
        });



    }


    public static void main (String[] args) {
        Log.set(Log.LEVEL_DEBUG);
        new PositionClient();
        boolean a= true;
        while(a) {
            if (false) {
                a = false;
            }
        };
    }




}
