package net.jyro.windows.world.pbr.entity;

import net.jyro.windows.world.pbr.model.AdvancedTexturedModel;
import org.joml.Vector3f;

public class PBREntity {

    private AdvancedTexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    private float metallic  = 1.0f;
    private float roughness = 1.0f;

    public PBREntity(AdvancedTexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void incrementPosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void incrementRotation(float drx, float dry, float drz) {
        this.rotX += drx;
        this.rotY += dry;
        this.rotZ += drz;
    }

    public AdvancedTexturedModel getModel() {
        return model;
    }

    public float getMetallic() {
        return metallic;
    }

    public float getRoughness() {
        return roughness;
    }

    public void setMetallic(float metallic) {
        this.metallic = metallic;
    }

    public void setRoughness(float roughness) {
        this.roughness = roughness;
    }

    public void setModel(AdvancedTexturedModel model) {
        this.model = model;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
