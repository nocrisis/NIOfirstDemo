import API.FlyBehavior;
import API.impl.DuckFlyBehaviorImpl;
import Client.RPCClient;


public class NettyRPCTest {

    public static void main(String[] args) {
        FlyBehavior duckFlyBehavior = new DuckFlyBehaviorImpl();
        FlyBehavior invoker =RPCClient.refer(duckFlyBehavior);
        invoker.fly("猪");
    }
}
