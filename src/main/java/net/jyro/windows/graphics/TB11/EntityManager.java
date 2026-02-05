package net.jyro.windows.graphics.TB11;

public class EntityManager {

    private static float[] triangleVertices;
    private static float[] cubeVertices;
    private static float[] cubeVertices2;
    private static float[] spatialVert;

    /** Load a single spinning triangle */
    public static void loadTriangle(float size) {
        float s = size;
        triangleVertices = new float[]{
            // x, y, z,   r, g, b
            0f,  s, 0f,  1f, 0f, 0f,
            -s, -s, 0f,  0f, 1f, 0f,
            s, -s, 0f,  0f, 0f, 1f
        };
    }

    /** Load a cube made of 12 triangles (6 faces × 2 triangles) */
    public static void loadCube(float size) {
        float s = size / 2f;

        cubeVertices = new float[]{
            // Front face (Z+)
            -s, -s,  s,  1, 0, 0,
            s, -s,  s,  0, 1, 0,
            s,  s,  s,  0, 0, 1,
            -s, -s,  s,  1, 0, 0,
            s,  s,  s,  0, 0, 1,
            -s,  s,  s,  1, 1, 0,

            // Back face (Z-)
            -s, -s, -s,  0, 1, 1,
            s, -s, -s,  1, 0, 1,
            s,  s, -s,  1, 1, 1,
            -s, -s, -s,  0, 1, 1,
            s,  s, -s,  1, 1, 1,
            -s,  s, -s,  0, 0, 0,

            // Left face (X-)
            -s, -s, -s,  1, 0, 0,
            -s, -s,  s,  0, 1, 0,
            -s,  s,  s,  0, 0, 1,
            -s, -s, -s,  1, 0, 0,
            -s,  s,  s,  0, 0, 1,
            -s,  s, -s,  1, 1, 0,

            // Right face (X+)
            s, -s, -s,  0, 1, 1,
            s, -s,  s,  1, 0, 1,
            s,  s,  s,  1, 1, 1,
            s, -s, -s,  0, 1, 1,
            s,  s,  s,  1, 1, 1,
            s,  s, -s,  0, 0, 0,

            // Top face (Y+)
            -s,  s, -s,  1, 0, 0,
            s,  s, -s,  0, 1, 0,
            s,  s,  s,  0, 0, 1,
            -s,  s, -s,  1, 0, 0,
            s,  s,  s,  0, 0, 1,
            -s,  s,  s,  1, 1, 0,

            // Bottom face (Y-)
            -s, -s, -s,  0, 1, 1,
            s, -s, -s,  1, 0, 1,
            s, -s,  s,  1, 1, 1,
            -s, -s, -s,  0, 1, 1,
            s, -s,  s,  1, 1, 1,
            -s, -s,  s,  0, 0, 0
        };
    }
    public static void loadSpatialVertexBasedModel(float size,float[] vertices) {
        float s = size;
        spatialVert = vertices;
    }
    /** Load a cube made of 12 triangles (6 faces × 2 triangles) */
    public static void loadSecondCube(float size) {
        float s = size / 2f;

        cubeVertices2 = new float[]{
            // Front face (Z+)
            -s, -s,  s,  1, 0, 0,
            s, -s,  s,  0, 1, 0,
            s,  s,  s,  0, 0, 1,
            -s, -s,  s,  1, 0, 0,
            s,  s,  s,  0, 0, 1,
            -s,  s,  s,  1, 1, 0,

            // Back face (Z-)
            -s, -s, -s,  0, 1, 1,
            s, -s, -s,  1, 0, 1,
            s,  s, -s,  1, 1, 1,
            -s, -s, -s,  0, 1, 1,
            s,  s, -s,  1, 1, 1,
            -s,  s, -s,  0, 0, 0,

            // Left face (X-)
            -s, -s, -s,  1, 0, 0,
            -s, -s,  s,  0, 1, 0,
            -s,  s,  s,  0, 0, 1,
            -s, -s, -s,  1, 0, 0,
            -s,  s,  s,  0, 0, 1,
            -s,  s, -s,  1, 1, 0,

            // Right face (X+)
            s, -s, -s,  0, 1, 1,
            s, -s,  s,  1, 0, 1,
            s,  s,  s,  1, 1, 1,
            s, -s, -s,  0, 1, 1,
            s,  s,  s,  1, 1, 1,
            s,  s, -s,  0, 0, 0,

            // Top face (Y+)
            -s,  s, -s,  1, 0, 0,
            s,  s, -s,  0, 1, 0,
            s,  s,  s,  0, 0, 1,
            -s,  s, -s,  1, 0, 0,
            s,  s,  s,  0, 0, 1,
            -s,  s,  s,  1, 1, 0,

            // Bottom face (Y-)
            -s, -s, -s,  0, 1, 1,
            s, -s, -s,  1, 0, 1,
            s, -s,  s,  1, 1, 1,
            -s, -s, -s,  0, 1, 1,
            s, -s,  s,  1, 1, 1,
            -s, -s,  s,  0, 0, 0
        };
    }

    public static float[] getTriangleVertices() {
        return triangleVertices;
    }

    public static float[] getCubeVertices() {
        return cubeVertices;
    }
    public static float[] getSecondCubeVertices() {
        return  cubeVertices2;
    }
    public static float[] getModelVertices() {
        return spatialVert;
    }
}
