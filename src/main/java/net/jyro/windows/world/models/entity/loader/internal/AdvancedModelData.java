package net.jyro.windows.world.models.entity.loader.internal;

public class AdvancedModelData {

    private final float[] vertices;
    private final float[] textureCoords;
    private final float[] normals;
    private final float[] tangents;
    private final int[] indices;
    private final float furthestPoint;

    private final int diffuseTextureId;
    private final int normalMapTextureId;
    private final int metallicRoughnessTextureId; // New: PBR texture ID

    public AdvancedModelData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents,
                     int[] indices, float furthestPoint, int diffuseTextureId,
                     int normalMapTextureId, int metallicRoughnessTextureId) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.tangents = tangents;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
        this.diffuseTextureId = diffuseTextureId;
        this.normalMapTextureId = normalMapTextureId;
        this.metallicRoughnessTextureId = metallicRoughnessTextureId; // Initialize
    }

    public float[] getVertices() { return vertices; }
    public float[] getTextureCoords() { return textureCoords; }
    public float[] getNormals() { return normals; }
    public float[] getTangents() { return tangents; }
    public int[] getIndices() { return indices; }
    public float getFurthestPoint() { return furthestPoint; }

    public int getDiffuseTextureId() { return diffuseTextureId; }
    public int getNormalMapTextureId() { return normalMapTextureId; }
    public int getMetallicRoughnessTextureId() { return metallicRoughnessTextureId; } // New Getter
}
