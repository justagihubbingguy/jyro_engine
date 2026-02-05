package net.jyro.windows.addons.physics;

public class GravityObject {
    public float x,y,vx,vy,m,ax,ay;
    public void update(float dt) {
        vx += ax * dt;
        vy += ay * dt;
        x +=  vx * dt;
        y += vy * dt;

    }
    public GravityObject(float x, float y, float vx,float vy,float m) {
        this.ax = 0;
        this.ay = 0;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.m = m;
    }
    public static void updatePhysics(GravityObject[] objects,float dt,float G) {
        for (GravityObject o : objects) {
            o.ax = 0;
            o.ay = 0;
        }
        for (int i = 0; i < objects.length; i++) {
            for (int j = i + 1; j < objects.length; j++) {
                float[] f = computeGravity(objects[i], objects[j],G);
                objects[i].ax += f[0] / objects[i].m;
                objects[i].ay += f[1] / objects[i].m;

                objects[j].ax -= f[0] / objects[j].m;
                objects[j].ay -= f[1] / objects[j].m;
            }
        }
        for (GravityObject o : objects) {
            o.update(dt);
        }
    }
    public static float[] computeGravity(GravityObject obj1, GravityObject obj2, float G) {
        float dx = obj2.x - obj1.x;
        float dy = obj2.y - obj1.y;
        float r2 = dx * dx + dy * dy + 0.01f;
        float F = G * obj1.m * obj2.m / r2;
        float r = (float)Math.sqrt(r2);
        return new float[]{F * dx / r, F * dy / r};
    }
}
