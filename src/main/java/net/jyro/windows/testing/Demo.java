package net.jyro.windows.testing;

import net.jyro.windows.world.models.entity.Entity;
import net.jyro.windows.world.models.TexturedModel;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Demo {
    private Vector3f planet2p;
    private Entity sun;
    private Vector3f planet1p;

    public Vector3f getPlanet1p() {
        return planet1p;
    }

    public Entity getSun() {
        return sun;
    }

    private static final float G = 2.5f;

    private final List<PhysicsPlanet> physicsPlanets = new ArrayList<>();

    public void createSolarSystem(
        TexturedModel sphereModel,
        Map<TexturedModel, List<Entity>> planetBatches,
        Map<TexturedModel, List<Entity>> atmosphereBatches
    ) {

        List<Entity> planetBatchList =
            planetBatches.computeIfAbsent(sphereModel, k -> new ArrayList<>());

        List<Entity> atmosphereBatchList =
            atmosphereBatches.computeIfAbsent(sphereModel, k -> new ArrayList<>());

        sun = new Entity(
            sphereModel,
            new Vector3f(0, 0, 0),
            0, 0, 0,
            3.0f
        );
        physicsPlanets.add(new PhysicsPlanet(sun, new Vector3f(0, 0, 0), 10000f));
        planetBatchList.add(sun);

        Entity planet1 = new Entity(
            sphereModel,
            new Vector3f(30, 10, 0),
            0, 0, 0,
            5f
        );
        physicsPlanets.add(new PhysicsPlanet(planet1, new Vector3f(0, 0, 45f), 10f));
        planetBatchList.add(planet1);
        planet1p = planet1.getPosition();

        Entity planet1Atmo = new Entity(
            sphereModel,
            planet1.getPosition(),
            0, 0, 0,
            0.8f * 1.05f
        );
        atmosphereBatchList.add(planet1Atmo);

        Entity planet2 = new Entity(
            sphereModel,
            new Vector3f(-80, 0, 0),
            0, 0, 0,
            1.2f
        );
        physicsPlanets.add(new PhysicsPlanet(planet2, new Vector3f(0, 0, -30f), 20f));
        planetBatchList.add(planet2);
        planet2p = planet2.getPosition();

        Entity planet2Atmo = new Entity(
            sphereModel,
            planet2.getPosition(),
            0, 0, 0,
            1.2f * 1.05f
        );
        atmosphereBatchList.add(planet2Atmo);
    }


    public List<PhysicsPlanet> getPhysicsPlanets() {
        return physicsPlanets;
    }
    public Vector3f getPlanet2X() {
        return planet2p;
    }

    public void update(float dt) {

        for (PhysicsPlanet p1 : physicsPlanets) {
            Vector3f netAcceleration = new Vector3f();

            for (PhysicsPlanet p2 : physicsPlanets) {
                if (p1 == p2 || p2.mass <= 0) continue;

                Vector3f diff = new Vector3f(p2.entity.getPosition())
                    .sub(p1.entity.getPosition());

                float distanceSq = diff.lengthSquared();

                if (distanceSq < 4.0f) continue;

                float accel = (G * p2.mass) / distanceSq;

                netAcceleration.add(diff.normalize().mul(accel));
            }

            p1.velocity.add(netAcceleration.mul(dt));
        }

        for (PhysicsPlanet p : physicsPlanets) {
            p.update(dt);
        }
    }
}
