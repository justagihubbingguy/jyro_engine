package net.jyro.windows.world.pbr.model;

import net.jyro.windows.world.models.StaticModel;

public class AdvancedTexturedModel {

    private StaticModel staticModel;
    private int diffuseTextureId;
    private int normalMapTextureId;
    private int metallicRoughnessTextureId;

    public AdvancedTexturedModel(
        StaticModel model,
        int diffuseTextureId,
        int normalMapTextureId,
        int metallicRoughnessTextureId
    ) {
        this.staticModel = model;
        this.diffuseTextureId = diffuseTextureId;
        this.normalMapTextureId = normalMapTextureId;
        this.metallicRoughnessTextureId = metallicRoughnessTextureId;
    }

    public StaticModel getModel() {
        return staticModel;
    }

    public int getDiffuseTextureId() {
        return diffuseTextureId;
    }

    public int getMetallicRoughnessTextureId() {
        return metallicRoughnessTextureId;
    }

    public int getNormalMapTextureId() {
        return normalMapTextureId;
    }
}
