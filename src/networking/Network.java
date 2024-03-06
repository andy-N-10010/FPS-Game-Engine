package networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

    static public final int port = 6334;

    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);

    }

    static public class SomeRequest {
        public String text;
    }
    static public class SomeResponse {
        public String text;
    }


    
}
