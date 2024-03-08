package networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

    static public final int port = 6334;

    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
        kryo.register(UpdateGameObjects.class);
        kryo.register(AddPlayer.class);
        kryo.register(MovePlayer.class);
        kryo.register(Login.class);
        kryo.register(RegistrationRequired.class);
        kryo.register(Register.class);
        kryo.register(RemovePlayer.class);
        kryo.register(Player.class);
        kryo.register(org.ode4j.math.DVector3.class);

    }

    static public class SomeRequest {
        public String text;
    }
    static public class SomeResponse {
        public String text;
    }

    static public class UpdateGameObjects {
        public int id, x, y, z;
    }

    static public class UpdatePlayer {
        public int id,x,y,z;
    }

    static public class AddPlayer {
        public Player player;
    }

    static public class MovePlayer {
        public int x, y, z;
    }

    static public class RemovePlayer {
        public int id;
    }


    static public class Login {
        public String username;
    }

    static public class RegistrationRequired {
    }

    static public class Register {
        public String username;
        public String otherStuff;
    }



    
}
