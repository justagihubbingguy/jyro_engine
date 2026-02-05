package net.jyro.windows.world.models;

public class StaticModel {
    private int vaoID;
    private int vertexCount;

    public StaticModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
