package networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import gameLoop.GravitySimulation;
import gameLoop.Input;
import gameLoop.PlayerSimulation;
import gameLoop.renderEngine;
import networking.screens.ConnectionScreen;
import networking.Network.Login;
import networking.Network.MovePlayer;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DVector3;

import networking.Network.Register;

import javax.swing.JOptionPane;

import java.io.IOException;
import java.util.HashMap;

public class PositionClient {
    Client client;
    UI ui;
    String username;

    private DVector3 position, rotation;

    public PositionClient() {
        client = new Client();
        client.start();

        Network.register(client);


//        Network.SomeRequest request = new Network.SomeRequest();
//        request.text = "Here is the request";
//        client.sendTCP(request);

        client.addListener(new Listener() {
            public void connected (Connection connection) {
            }

            public void received (Connection connection, Object object) {
                    if (object instanceof Network.SomeResponse) {
                        Network.SomeResponse response = (Network.SomeResponse)object;
                        System.out.println(response.text);
                    }

                    if (object instanceof Network.RegistrationRequired) {
                        Register register = new Register();
                        register.username = username;
                        client.sendTCP(register);
                    }

                    if (object instanceof Network.AddPlayer) {
                        Network.AddPlayer msg = (Network.AddPlayer)object;
                        ui.addPlayer(msg.player);
                        return;
                    }

                    if (object instanceof Network.UpdatePlayer) {
                        ui.updatePlayer((Network.UpdatePlayer) object);
                        return;
                    }

                    if (object instanceof Network.RemovePlayer) {
                        Network.RemovePlayer msg = (Network.RemovePlayer)object;
                        ui.removePlayer(msg.id);
                        return;
                    }



            }



            public void disconnected (Connection connection) {
                System.exit(0);
            }
        });

        ui = new UI();

        String host = ui.inputHost();


        try {
            client.connect(5000, host, Network.port);
            // Server communication after connection can go here, or in Listener#connected().
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        username = ui.inputName();

        Login login = new Login();
        login.username = username;
        client.sendTCP(login);

        //new GravitySimulation().start();

        new PlayerSimulation().start();

//        while (true) {
//
//            if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = position.add(new DVector3(-z,0,x));
//
//            int ch;
//            try {
//                ch = System.in.read();
//
//            } catch(IOException ex) {
//                ex.printStackTrace();
//                break;
//            }
//
//            MovePlayer msg = new MovePlayer();
//            switch (ch) {
//                case 'w':
//                    msg.y  = -1;
//                    break;
//                case 's':
//                    msg.y  = 1;
//                    break;
//                case 'a':
//                    msg.x  = -1;
//                    break;
//                case 'd':
//                    msg.x = 1;
//                    break;
//                default :
//                    msg = null;
//            }
//            if (msg != null) client.sendTCP(msg);

//            new PlayerSimulation().start();
//
//
//        }

//        new PlayerSimulation().start();






    }

    static class UI {
        HashMap<Integer,Player> players = new HashMap<>();

        public String inputHost() {
            String input = (String)JOptionPane.showInputDialog(null, "Host:","Connect to server", JOptionPane.QUESTION_MESSAGE,
                    null,null,"localhost");
            if (input == null || input.trim().length() == 0) System.exit(1);
            return input.trim();
        }

        public String inputName() {
            String input = (String)JOptionPane.showInputDialog(null, "Name:", "Connect to server", JOptionPane.QUESTION_MESSAGE,
                    null,null,"Test");
            if (input == null || input.trim().length() == 0) System.exit(1);
            return input.trim();
        }

        public void addPlayer(Player player) {
            players.put(player.id,player);
            System.out.println(player.username + " added at "+ player.x + ", "+player.y + ", "+player.z);
        }

        public void updatePlayer(Network.UpdatePlayer msg) {
            Player player = players.get(msg.id);
            if (player == null) return;
            player.x = msg.x;
            player.y = msg.y;
            player.z = msg.z;
            System.out.println(player.username + " moved to "+ player.x + ", "+player.y +", "+player.z);

        }

        public void removePlayer (int id) {
            Player player = players.remove(id);
            if (player != null) System.out.println(player.username + " removed");
        }
    }


    public static void main (String[] args) {
        Log.set(Log.LEVEL_DEBUG);
        new PositionClient();

        //new GravitySimulation().start();
        //new renderEngine().start();

//        boolean a= true;

//        while(a) {
//            if (false) {
//                a = false;
//            }
//        };
    }




}
