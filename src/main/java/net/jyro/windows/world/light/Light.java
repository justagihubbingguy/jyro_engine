package net.jyro.windows.world.light;

import org.joml.Vector3f;

public class Light {
    private Vector3f position;

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Light(Vector3f color, Vector3f position) {
        this.color = color;
        this.position = position;
    }

    private Vector3f color;
}
