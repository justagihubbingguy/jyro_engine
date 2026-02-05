package net.jyro.windows.testing;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import net.jyro.windows.gui.backends.User32Ex;
import net.jyro.windows.world.SpatialWindows;
import net.jyro.windows.world.World;
import net.jyro.windows.world.ModelLoader;
import net.jyro.windows.world.SpatialRenderer;
import net.jyro.windows.world.camera.CameraViewer;
import net.jyro.windows.world.camera.CameraWindows;
import net.jyro.windows.world.light.Light;
import net.jyro.windows.world.models.StaticModel;
import net.jyro.windows.world.models.TexturedModel;
import net.jyro.windows.world.models.entity.Entity;
import net.jyro.windows.world.models.entity.loader.GeneralEntityLoader;
import net.jyro.windows.world.models.entity.loader.internal.ModelData;
import net.jyro.windows.world.shaders.DefaultShader;
import net.jyro.windows.world.texture.SpatialTexture;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class WorldTesting extends World {

    private DefaultShader shader;
    private Light light;
    private Entity sun;
    private Vector3f sunPosition = new Vector3f(10, 70, 10);
    private ModelLoader loader;
    private SpatialWindows renderer;
    private Entity entity;
    private CameraWindows camera;
    private Map<TexturedModel, List<Entity>> batches = new HashMap<>();

    private float lightRadius = 50f;       // orbit radius
    private float lightSpeed = 0.5f;       // radians/sec
    private float lightAngle = 0;
    private float lightHeight = 70f;

    public WorldTesting() {
        super("Win32 World Test", 800, 600);
    }

    @Override
    protected void setup() {
        batches = new HashMap<>(); // missing

        User32Ex.INSTANCE.ShowCursor(false);
        shader = new DefaultShader();
        loader = new ModelLoader();
        renderer = new SpatialWindows(shader,getWindowHandle());
        camera = new CameraWindows(getWindowHandle());

        // Load models
        ModelData modelData = GeneralEntityLoader.loadModel("C:\\Users\\Hp\\Downloads\\tabiom\\res\\scene.gltf");
        ModelData sunData   = GeneralEntityLoader.loadModel("C:\\Users\\Hp\\Downloads\\tabiom\\res\\textures\\stars\\scene.gltf");

        StaticModel models = loader.loadToVertexArray(
            modelData.getVertices(),
            modelData.getIndices(),
            modelData.getNormals(),
            modelData.getTextureCoords(),
            modelData.getTangents()
        );

        StaticModel sunModel = loader.loadToVertexArray(
            sunData.getVertices(),
            sunData.getIndices(),
            sunData.getNormals(),
            sunData.getTextureCoords(),
            sunData.getTangents()
        );

        TexturedModel sunTexModel = new TexturedModel(
            sunModel,
            sunData.getDiffuseTextureId(),
            sunData.getNormalMapTextureId()
        );

        SpatialTexture texture = new SpatialTexture(
            modelData.getDiffuseTextureId(),
            modelData.getNormalMapTextureId(),0
        );

        TexturedModel texturedModel = new TexturedModel(
            models,
            texture.getDiffuseTexture(),
            texture.getNormalMapTexture()
        );

        entity = new Entity(texturedModel, new Vector3f(0,0,-5), -90,0,0, 0.08f);
        sun = new Entity(sunTexModel, sunPosition, 0,0,0, 0.2f);

        light = new Light(new Vector3f(1.25f, 0.78f, 0.42f), sunPosition);

        batches.clear();
        batches.put(entity.getModel(), List.of(entity));
        batches.put(sun.getModel(), List.of(sun));

        glEnable(GL_DEPTH_TEST);
    }
    private long lastTime = System.nanoTime();

    @Override
    protected void draw() {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
        lastTime = currentTime;
        camera.updateMouseLook();
        camera.move(deltaTime);

        lightAngle += lightSpeed * deltaTime;
        if (lightAngle > 2*Math.PI) lightAngle -= 2*Math.PI;

        float lx = (float)Math.cos(lightAngle) * lightRadius;
        float lz = (float)Math.sin(lightAngle) * lightRadius;
        Vector3f newPos = new Vector3f(lx, lightHeight, lz);
        light.getPosition().set(newPos);
        sunPosition.set(newPos);
        sun.setPosition(newPos);

        // render
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadLight(light);
        renderer.render(batches, shader);
        shader.stop();
    }

    @Override
    protected void init() {

    }

    @Override
    protected void render() {

    }

    @Override
    protected void cleanup() {
        loader.cleanUpVertexData();
        shader.cleanUp();
    }

    public static void main(String[] args) {
        try {
            new WorldTesting().renderLoop();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
