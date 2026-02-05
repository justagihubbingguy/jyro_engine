package net.jyro.windows.testing;

import net.jyro.windows.world.models.entity.Entity;
import org.joml.Vector3f;

public class PhysicsPlanet {
    public Entity entity;
    public Vector3f velocity;
    public float mass;

    public PhysicsPlanet(Entity entity, Vector3f velocity, float mass) {
        this.entity = entity;
        this.velocity = velocity;
        this.mass = mass;
    }

    public void update(float dt) {
        // Integrate velocity into the actual entity's position
        Vector3f currentPos = entity.getPosition();
        currentPos.add(new Vector3f(velocity).mul(dt));
    }
}
