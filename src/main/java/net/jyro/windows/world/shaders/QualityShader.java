package net.jyro.windows.world.shaders;

import net.jyro.windows.world.camera.CameraView;
import net.jyro.windows.world.camera.CameraViewer;
import net.jyro.windows.world.camera.CameraWindows;
import net.jyro.windows.world.light.Light;
import net.jyro.windows.world.tools.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class QualityShader extends ShaderFactory {

    private static final String VERTEX_FILE = "C:\\Users\\Hp\\Downloads\\tabiom\\src\\main\\java\\net\\jyro\\windows\\world\\shaders\\backendsGlsl\\std\\vertexShader.glsl";
    private static final String FRAGMENT_FILE = "C:\\Users\\Hp\\Downloads\\tabiom\\src\\main\\java\\net\\jyro\\windows\\world\\shaders\\backendsGlsl\\pbr\\qualityFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_cameraPosition;

    private int location_diffuseSampler;
    private int location_normalMapSampler;
    private int location_metallicRoughnessSampler;

    private int location_metallic;
    private int location_roughness;

    public QualityShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix     = super.getUniformLocation("projectionMatrix");
        location_viewMatrix           = super.getUniformLocation("viewMatrix");
        location_lightPosition        = super.getUniformLocation("lightPosition");
        location_lightColor           = super.getUniformLocation("lightColor");
        location_cameraPosition       = super.getUniformLocation("cameraPosition");

        location_diffuseSampler           = super.getUniformLocation("textureSampler");
        location_normalMapSampler         = super.getUniformLocation("normalMapSampler");
        location_metallicRoughnessSampler = super.getUniformLocation("metallicRoughnessSampler");

        location_metallic  = super.getUniformLocation("metallic");
        location_roughness = super.getUniformLocation("roughness");
    }

    @Override
    protected void bindAtrribs() {
        super.bindAttrib(0, "position");
        super.bindAttrib(1, "textureCoords");
        super.bindAttrib(2, "normal");
        super.bindAttrib(3, "tangent");
    }

    // --- Loading Methods ---

    public void loadPBRFactors(float metallic, float roughness) {
        super.loadFloat(location_metallic, metallic);
        super.loadFloat(location_roughness, roughness);
    }

    public void loadCameraPosition(Vector3f cameraPos) {
        super.loadVector(location_cameraPosition, cameraPos);
    }

    public void loadViewMatrix(CameraView camera) {
        Matrix4f matrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadViewMatrix(CameraViewer viewer) {
        super.loadMatrix(location_viewMatrix, viewer.getViewMatrix());
    }

    public void loadViewMatrix(CameraWindows windows) {
        super.loadMatrix(location_viewMatrix, Maths.createViewMatrix(windows));
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
    }

    public void loadLight(Light light) {
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }

    public void loadTextureUnits() {
        super.loadInt(location_diffuseSampler, 0);
        super.loadInt(location_normalMapSampler, 1);
        super.loadInt(location_metallicRoughnessSampler, 2);
    }
}
