package net.jyro.windows.world.models.entity.loader.internal;

public class ModelData {

    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private float[] tangents;       // new
    private int[] indices;
    private float furthestPoint;
    private int diffuseTextureId;   // new
    private int normalMapTextureId; // new

    public ModelData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents,
                     int[] indices, float furthestPoint, int diffuseTextureId, int normalMapTextureId) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.tangents = tangents;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
        this.diffuseTextureId = diffuseTextureId;
        this.normalMapTextureId = normalMapTextureId;
    }

    public float[] getVertices() { return vertices; }
    public float[] getTextureCoords() { return textureCoords; }
    public float[] getNormals() { return normals; }
    public float[] getTangents() { return tangents; }
    public int[] getIndices() { return indices; }
    public float getFurthestPoint() { return furthestPoint; }
    public int getDiffuseTextureId() { return diffuseTextureId; }
    public int getNormalMapTextureId() { return normalMapTextureId; }

}
