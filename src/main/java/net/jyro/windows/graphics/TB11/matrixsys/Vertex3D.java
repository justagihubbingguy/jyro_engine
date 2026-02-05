package net.jyro.windows.graphics.TB11.matrixsys;

public class Vertex3D {
    public float x, y, z, u, v;
    public int color;
    public Vertex3D(float x, float y, float z) {
        this(x, y, z, 0, 0);
    }
    public Vertex3D(float x, float y, float z, float u, float v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.u = u;
        this.v = v;
    }
}
