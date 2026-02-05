package net.jyro.windows.testing;

import net.jyro.windows.gui.Application;
import net.jyro.windows.gui.ApplicationProperties;
import net.jyro.windows.addons.physics.GravityObject;

public class Gravity extends Application {
    private final float G = 1.0f;
    private final float dt = 0.1f;
    private GravityObject[] objects;
    public Gravity(ApplicationProperties properties) {
        super(properties);
        objects = new GravityObject[]{
            new GravityObject(250, 152, 0.25f, 0f, 1),
            new GravityObject(250, 100, 2f, 0f, 50),
            new GravityObject(250, 250, 0f, 0f, 1000)
        };
        batch().setViewport(properties.getConcurrentApplicationWidth(),properties.getConcurrentApplicationHeight());
    }

    @Override
    protected void draw() {
        for (GravityObject o : objects) {
            float radius = (float)Math.sqrt(o.m);
            batch().drawCirclePixels(o.x - radius / 2, o.y - radius / 2, radius, radius,0xFF0000,32);
        }
        GravityObject.updatePhysics(objects,dt,G);
    }
    public static void main(String[] args) throws InterruptedException {
        launch(new Gravity(new ApplicationProperties("Gravity",500,500)));
    }
}
