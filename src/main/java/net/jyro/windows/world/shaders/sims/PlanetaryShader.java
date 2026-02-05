package net.jyro.windows.world.shaders.sims;

import net.jyro.windows.world.camera.CameraViewer;
import net.jyro.windows.world.shaders.ShaderFactory;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PlanetaryShader extends ShaderFactory {

    private static final String VERTEX_FILE =
        "C:\\Users\\Hp\\Downloads\\tabiom\\src\\main\\java\\net\\jyro\\windows\\world\\shaders\\backendsGlsl\\addon\\planetaryVertex.glsl";
    private static final String FRAGMENT_FILE =
        "C:\\Users\\Hp\\Downloads\\tabiom\\src\\main\\java\\net\\jyro\\windows\\world\\shaders\\backendsGlsl\\addon\\planetaryShader.glsl";

    // Matrices
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    // Scene
    private int location_cameraPos;
    private int location_sunDirection;

    // Planet properties
    private int location_planetRadius;
    private int location_atmosphereRadius;
    private int location_surfaceColor;

    // Atmosphere parameters
    private int location_densityFalloff;
    private int location_numSteps;
    private int location_intensity;

    public PlanetaryShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix     = super.getUniformLocation("projectionMatrix");
        location_viewMatrix           = super.getUniformLocation("viewMatrix");

        location_cameraPos    = super.getUniformLocation("cameraPos");
        location_sunDirection = super.getUniformLocation("sunDir");

        location_planetRadius     = super.getUniformLocation("planetRadius");
        location_atmosphereRadius = super.getUniformLocation("atmosphereRadius");
        location_surfaceColor     = super.getUniformLocation("surfaceColor");

        location_densityFalloff = super.getUniformLocation("densityFalloff");
        location_numSteps       = super.getUniformLocation("numSteps");
        location_intensity      = super.getUniformLocation("intensity");
    }

    @Override
    protected void bindAtrribs() {
        super.bindAttrib(0, "position");
        super.bindAttrib(1, "textureCoords");
        super.bindAttrib(2, "normal");
    }

    // Matrix setters
    public void loadTransformationMatrix(Matrix4f matrix) { super.loadMatrix(location_transformationMatrix, matrix); }
    public void loadProjectionMatrix(Matrix4f projection) { super.loadMatrix(location_projectionMatrix, projection); }
    public void loadViewMatrix(CameraViewer camera) { super.loadMatrix(location_viewMatrix, camera.getViewMatrix()); }

    // Scene setters
    public void loadCameraPosition(Vector3f camPos) { super.loadVector(location_cameraPos, camPos); }
    public void loadSunDirection(Vector3f sunDir) {
        Vector3f dir = new Vector3f(sunDir).normalize();
        super.loadVector(location_sunDirection, dir);
    }

    // Planet setters
    public void loadPlanetRadii(float planetRadius, float atmosphereRadius) {
        super.loadFloat(location_planetRadius, planetRadius);
        super.loadFloat(location_atmosphereRadius, atmosphereRadius);
    }
    public void loadSurfaceColor(Vector3f color) { super.loadVector(location_surfaceColor, color); }

    // Atmosphere parameters
    public void loadDensityFalloff(float falloff) { super.loadFloat(location_densityFalloff, falloff); }
    public void loadNumSteps(int steps) { super.loadInt(location_numSteps, steps); }
    public void loadIntensity(float intensity) { super.loadFloat(location_intensity, intensity); }
}
