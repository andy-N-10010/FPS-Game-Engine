package networking.screens;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
//import gameLoop.renderEngine;

import java.io.IOException;
import java.util.Scanner;

public class ConnectionScreen {
    private String TCP;
    private String port;
    private String username;

    private String UDP;


    public ConnectionScreen(String tcp, String userport, String name, String udp) {

        TCP = tcp;
        port = userport;
        username = name;
        UDP = udp;

    }

    public void establishConnection() {

        final Client client = new Client();

        try {
            client.start();
            client.connect(15000, TCP, Integer.parseInt(port), Integer.parseInt(UDP));
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

        ConnectionScreen connectionScreen = new ConnectionScreen(IPAddress,port,username,IPAddress);

        connectionScreen.establishConnection();


    }

}
