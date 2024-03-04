package networking.screens;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import gameLoop.renderEngine;

import java.io.IOException;
import java.util.Scanner;

public class ConnectionScreen {
    private String IP_Address;
    private String port;
    private String username;


    public ConnectionScreen(String ipAddress, String userport, String name) {

        IP_Address = ipAddress;
        port = userport;
        username = name;

    }

    public void establishConnection() {

        final Client client = new Client();

        try {
            client.start();
            client.connect(15000, IP_Address, Integer.parseInt(port));
            System.out.println("Established Connection");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter IP address");

        String IPAddress = input.nextLine();

        System.out.println("Enter port");

        String port = input.nextLine();

        System.out.println("Enter username");

        String username = input.nextLine();

        ConnectionScreen connectionScreen = new ConnectionScreen(IPAddress,port,username);

        connectionScreen.establishConnection();


    }

}
