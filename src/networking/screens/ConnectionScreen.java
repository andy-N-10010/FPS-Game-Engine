package networking.screens;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class ConnectionScreen {
    private String IP_Address;
    private String port;
    private String username;


    public ConnectionScreen() {
    }

    public void establishConnection() {

        final Client client = new Client();

        try {
            client.connect(15000, IP_Address, Integer.valueOf(port));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
