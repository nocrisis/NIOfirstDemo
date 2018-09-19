package NIORPC.server;

import API.impl.DuckFlyBehaviorImpl;

public class serverTest {
    public static void main(String[] args) {
        RPCServer server = RPCServer.getInstance();
        server.addClass(new DuckFlyBehaviorImpl());
        server.start();
    }
}
