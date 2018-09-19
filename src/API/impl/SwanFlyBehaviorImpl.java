package API.impl;

import API.FlyBehavior;

public class SwanFlyBehaviorImpl implements FlyBehavior {
    @Override
    public String fly(String bird) {
        return "优雅的" + bird + "在天上飞";
    }
}
