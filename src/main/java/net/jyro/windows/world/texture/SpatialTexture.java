package net.jyro.windows.world.texture;

public class SpatialTexture {
    private final int diffuseTexture;
    private final int normalMapTexture;
    private final int metallicRoughnessTexture;

    public SpatialTexture(int diffuseTexture, int normalMapTexture,int metallicRoughnessTexture) {
        this.diffuseTexture = diffuseTexture;
        this.normalMapTexture = normalMapTexture;
        this.metallicRoughnessTexture = metallicRoughnessTexture;
    }

    public int getDiffuseTexture() { return diffuseTexture; }
    public int getNormalMapTexture() { return normalMapTexture; }

    public int getMetallicRoughnessTexture() {
        return metallicRoughnessTexture;
    }
}
