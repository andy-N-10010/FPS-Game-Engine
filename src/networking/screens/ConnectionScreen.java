package networking.screens;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
//import gameLoop.renderEngine;
import com.esotericsoftware.kryonet.Listener;
import gameLoop.renderEngine;
import networking.ServerFoundation;

import java.io.IOException;
import java.util.Scanner;

public class ConnectionScreen {
    private String TCP;
    private String port;
    private String username;

    private String UDP;

    Client client;

    //private ServerFoundation.SomeRequest request;

    //public someRequest request;

    public class SomeRequest {
        public String text;
    }
    public class SomeResponse {
        public String text;
    }


    public ConnectionScreen(String tcp, String userport, String name, String udp) {

        TCP = tcp;
        port = userport;
        username = name;
        UDP = udp;

        try {

        client = new Client();

        client.start();

        client.connect(15000, port,Integer.parseInt(TCP), Integer.parseInt(UDP));

        SomeRequest request = new SomeRequest();
        request.text = "Here is the request";
        client.sendTCP(request);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        client.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SomeResponse) {
                    SomeResponse response = (SomeResponse)object;
                    System.out.println(response.text);
                }
            }
        });


    }

    public void establishConnection() {

//        final Client client = new Client();
//
//        try {
//            client.start();
//            client.connect(15000, port,Integer.parseInt(TCP), Integer.parseInt(UDP));
//            //client.connect(15000, TCP, Integer.parseInt(port), Integer.parseInt(UDP));
//            System.out.println("Established Connection");
//
//            //ServerFoundation.SomeRequest request = new ServerFoundation.SomeRequest();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }

    }

//    public void sendRequest () {
//        request = new someRequest();
//        request.text = "Here is the request";
//        client.sendTCP(request);
//
//    }

//    public class someRequest {
//        public String text;
//    }
//    public class someResponse {
//        public String text;
//    }


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter tcp port");

        String tcp = input.nextLine();

        System.out.println("Enter IPAddress");

        String IPAddress = input.nextLine();

        System.out.println("Enter username");

        String username = input.nextLine();

        System.out.println("Enter udp port");

        String udp = input.nextLine();

        ConnectionScreen connectionScreen = new ConnectionScreen(tcp,IPAddress,username, udp);

        //connectionScreen.sendRequest();

        //connectionScreen.establishConnection();


    }

}
