package networking;


import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.*;
import java.util.HashSet;


public class PositionServer {
    private Server server;
    HashSet<Player> loggedIn = new HashSet<>();

    public PositionServer () throws IOException {
        server = new Server() {

            protected  Connection newConnection() {
                return new PlayerConnection();
            }
        };

        Network.register(server);



        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {

                if (object instanceof Network.SomeRequest) {

                    Network.SomeRequest request = (Network.SomeRequest) object;
                    System.out.println(request.text);

                    Network.SomeResponse response = new Network.SomeResponse();
                    response.text = "Thanks";
                    connection.sendTCP(response);

                }

                PlayerConnection connect = (PlayerConnection)connection;

                Player player = connect.player;

                if (object instanceof Network.Login) {

                        if (player != null) return;

                        String name = ((Network.Login)object).username;

                        player = loadPlayer(name);

                        if (player == null) {
                            connect.sendTCP(new Network.RegistrationRequired());
                            return;
                        }

                        loggedIn(connect,player);
                        return;

                }

                if (object instanceof Network.Register) {

                    if (player != null) return;

                    Network.Register register = (Network.Register)object;

                    if (loadPlayer(register.username)!= null) {
                        connect.close();
                        return;
                    }

                    player = new Player();
                    player.username = register.username;
                    player.x = 0;
                    player.y = 0;
                    player.z = 0;
                    if (!savePlayer(player)) {
                        connect.close();
                        return;
                    }

                    loggedIn(connect, player);
                    return;

                }

                if (object instanceof Network.MovePlayer) {

                    if (player == null) return;

                    Network.MovePlayer msg = (Network.MovePlayer)object;

                    if (Math.abs(msg.x) != 1 && Math.abs(msg.y) != 1 && Math.abs(msg.z) != 1) return;

                    player.x += msg.x;
                    player.y += msg.y;
                    player.z += msg.z;
                    if (!savePlayer (player)) {
                        connect.close();
                        return;
                    }

                    Network.UpdatePlayer update = new Network.UpdatePlayer();
                    update.id = player.id;
                    update.x = player.x;
                    update.y = player.y;
                    update.z = player.z;
                    server.sendToAllTCP(update);
                    return;


                }

            }
            private boolean isValid (String value) {
                if (value == null) return false;
                value = value.trim();
                if (value.length() == 0) return false;
                return true;
            }


            public void disconnected(Connection connect) {
                PlayerConnection connection = (PlayerConnection) connect;
                if (connection.player != null) {
                    loggedIn.remove(connection.player);

                    Network.RemovePlayer removePlayer = new Network.RemovePlayer();
                    removePlayer.id = connection.player.id;
                    server.sendToAllTCP(removePlayer);

                }
            }
        });


        server.start();

        server.bind(Network.port);


    }

    void loggedIn (PlayerConnection c, Player player) {
        c.player = player;

        for (Player other : loggedIn) {
            Network.AddPlayer addPlayer = new Network.AddPlayer();
            addPlayer.player = other;
            c.sendTCP(addPlayer);
        }

        loggedIn.add(player);

        Network.AddPlayer addPlayer = new Network.AddPlayer();
        addPlayer.player = player;
        server.sendToAllTCP(addPlayer);

    }

    boolean savePlayer (Player player) {
        File file = new File("players", player.username.toLowerCase());
        file.getParentFile().mkdirs();

        if (player.id == 0) {
            String[] children = file.getParentFile().list();
            if (children == null) return false;
            player.id = children.length + 1;
        }

        DataOutputStream output = null;

        try {
            output = new DataOutputStream(new FileOutputStream(file));
            output.writeInt(player.id);
            output.writeInt(player.x);
            output.writeInt(player.y);
            output.writeInt(player.z);
            return true;

        }catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                output.close();
            } catch (IOException ignored) {

            }
        }
    }


    Player loadPlayer(String name) {
        Player player = new Player();
        player.username = name;
        player.x = 0;
        player.y = 0;
        player.z = 1;
        return player;

//        File file = new File("Players", name.toLowerCase());
//        if (!file.exists()) return null;
//        DataInputStream input = null;
//
//        try {
//            input = new DataInputStream(new FileInputStream((file)));
//            Player player = new Player();
//            player.id = input.readInt();
//            player.username = name;
//            player.x = input.readInt();
//            player.y = input.readInt();
//            player.z = input.readInt();
//            input.close();
//            return player;
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        } finally {
//            try {
//                if (input != null) input.close();
//            }catch(IOException ignored) {
//
//            }
//        }

    }

    static class PlayerConnection extends Connection {
        public Player player;
    }

    public static void main (String[] args) throws IOException {
        //Log.set(Log.LEVEL_DEBUG);
        new PositionServer();
    }

}
