package networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

    static public final int port = 6334;

    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
        kryo.register(UpdateGameObject.class);
        kryo.register(AddPlayer.class);
        kryo.register(MovePlayer.class);
        kryo.register(Login.class);
        kryo.register(RegistrationRequired.class);
        kryo.register(Register.class);
        kryo.register(RemovePlayer.class);
        kryo.register(Player.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(org.ode4j.math.DVector3.class);
        kryo.register(MoveObject.class);
        kryo.register(detectObject.class);
        kryo.register(UpdateObject.class);

    }

    static public class SomeRequest {
        public String text;
    }
    static public class SomeResponse {
        public String text;
    }

    static public class UpdateGameObject {
        public int id;
        public float x,y,z;
    }

    static public class UpdatePlayer {
        public int id;
        public float x, y, z;
    }

    static public class UpdateObject {
        public int id;
        public float x, y, z;
    }


    static public class AddPlayer {
        public Player player;
    }


    static public class AddObject {

        public GameObject object;
    }

    static public class MoveObject {
        public float x, y, z;
    }

    static public class MovePlayer {
        public float x, y, z;
    }

    static public class RemovePlayer {
        public int id;
    }


    static public class Login {
        public String username;
    }

    static public class detectObject {
        public String name;
    }

    static public class RegistrationRequired {
    }

    static public class Register {
        public String username;
        public String otherStuff;
    }



    
}
