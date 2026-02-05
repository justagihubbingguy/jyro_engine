package net.jyro.windows.world.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SunShader extends ShaderFactory {

    private static final String VERTEX_FILE =
        "C:\\Users\\Hp\\Downloads\\tabiom\\src\\main\\java\\net\\jyro\\windows\\world\\shaders\\backendsGlsl\\sun\\sunVertex.glsl";
    private static final String FRAGMENT_FILE =
        "C:\\Users\\Hp\\Downloads\\tabiom\\src\\main\\java\\net\\jyro\\windows\\world\\shaders\\backendsGlsl\\sun\\sunFragment.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    private int location_sunColor;
    private int location_cameraPos;
    private int location_glowRadius;
    private int location_sunPosition;

    public SunShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix     = super.getUniformLocation("projectionMatrix");
        location_viewMatrix           = super.getUniformLocation("viewMatrix");

        location_sunColor      = super.getUniformLocation("sunColor");
        location_cameraPos     = super.getUniformLocation("cameraPos");
        location_glowRadius    = super.getUniformLocation("glowRadius");
        location_sunPosition   = super.getUniformLocation("sunPosition");
    }

    @Override
    protected void bindAtrribs() {
        super.bindAttrib(0, "position");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadSunColor(Vector3f color) {
        super.loadVector(location_sunColor, color);
    }

    public void loadCameraPosition(Vector3f camPos) {
        super.loadVector(location_cameraPos, camPos);
    }

    public void loadGlowRadius(float radius) {
        super.loadFloat(location_glowRadius, radius);
    }

    public void loadSunPosition(Vector3f pos) {
        super.loadVector(location_sunPosition, pos);
    }
}
