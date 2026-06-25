package com.economysmp.homes;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class Home {
    public String name;
    public double x, y, z;
    public String world;
    public float yaw, pitch;

    public Home(String name, double x, double y, double z, String world, float yaw, float pitch) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
