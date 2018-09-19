package API.impl;

import API.FlyBehavior;

public class DuckFlyBehaviorImpl implements FlyBehavior {
    @Override
    public String fly(String bird) {
        return "家养的" +bird + "不会飞";
    }
}
