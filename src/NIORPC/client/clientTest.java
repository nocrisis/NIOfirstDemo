package NIORPC.client;

import API.FlyBehavior;

public class clientTest {
    public static void main(String[] args) {
        RPCClient client = RPCClient.getInstance();
        client.init("127.0.0.1");
        FlyBehavior sayHello =  (FlyBehavior) client.getRemoteProxy(FlyBehavior.class);
        System.out.println("client:"+sayHello.fly("duck"));
    }
}
