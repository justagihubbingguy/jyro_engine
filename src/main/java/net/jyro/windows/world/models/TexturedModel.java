package net.jyro.windows.world.models;

public class TexturedModel {

    private StaticModel staticModel;
    private int diffuseTextureId;
    private int normalMapTextureId;

    public TexturedModel(
        StaticModel model,
        int diffuseTextureId,
        int normalMapTextureId
    ) {
        this.staticModel = model;
        this.diffuseTextureId = diffuseTextureId;
        this.normalMapTextureId = normalMapTextureId;
    }

    public StaticModel getModel() {
        return staticModel;
    }

    public int getDiffuseTextureId() {
        return diffuseTextureId;
    }

    public int getNormalMapTextureId() {
        return normalMapTextureId;
    }
}
