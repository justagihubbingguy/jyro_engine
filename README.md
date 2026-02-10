# 🌌 Java Game & Simulation Engine

A hobby **2D and 3D game & simulation engine** built in **Java** using **LWJGL** (Lightweight Java Game Library)..

---

## 🚀 Features

### 3D Engine
- Fully-featured **3D rendering pipeline** using OpenGL.
- **Lighting & shading** supports ambient lighting, High Quality PBR Graphics.
- **Model loading** (OBJ, FBX, and other formats).
- **Camera system** with free movement and first-person controls.

### 2D Engine
- **Sprite rendering** with batch optimization.-

### Cross-cutting Features
- **Input handling** for keyboard, mouse, and controllers.
- **Modular architecture** for easy extension and customization.
- Optimized for **high FPS** and low-latency simulations.

---

## Examples
    
```java 
package net.jyro.windows.testing;

import net.jyro.windows.world.ModelLoader;
import net.jyro.windows.world.SpatialRenderer;
import net.jyro.windows.world.camera.CameraViewer;
import net.jyro.windows.world.camera.CameraWindows;
import net.jyro.windows.world.camera.MouseInteractor;
import net.jyro.windows.world.light.Light;
import net.jyro.windows.world.models.StaticModel;
import net.jyro.windows.world.models.TexturedModel;
import net.jyro.windows.world.models.entity.Entity;
import net.jyro.windows.world.models.entity.loader.GeneralEntityLoader;
import net.jyro.windows.world.models.entity.loader.internal.ModelData;
import net.jyro.windows.world.shaders.DefaultShader;
import net.jyro.windows.world.shaders.sims.PlanetaryShader;
import net.jyro.windows.world.texture.SpatialTexture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainViewer {

    private long window;
    private int width = 900;
    private int height = 700;

    private ModelLoader loader;
    private CameraViewer camera;
    private SpatialRenderer renderer;
    private Light light;
    private DefaultShader shader;

    private Map<TexturedModel, List<Entity>> objectBatches = new HashMap<>();
    private Entity objectEntity;

    public static void main(String[] args) {
        new MainViewer().run();
    }

    public void run() {
        initWindow();
        initScene();
        loop();
        cleanUp();
    }

    private void initWindow() {
        if (!GLFW.glfwInit()) throw new IllegalStateException("Failed to init GLFW");

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        window = GLFW.glfwCreateWindow(width, height, "Single Object Viewer", 0, 0);
        if (window == 0) throw new RuntimeException("Window creation failed");

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glClearColor(0.5f, 0.58f, 0.55f, 1f);

        GL11.glCullFace(GL11.GL_BACK);
    }

    private void initScene() {
        loader = new ModelLoader();
        camera = new CameraViewer(window);
        shader = new DefaultShader();
        renderer = new SpatialRenderer(shader);
        ModelData model = GeneralEntityLoader.loadModel("C:\\Users\\Hp\\Downloads\\tabiom\\res\\cs\\scene.gltf");
        StaticModel smodel = loader.loadToVertexArray(
            model.getVertices(),
            model.getIndices(),
            model.getNormals(),
            model.getTextureCoords(),
            model.getTangents()
        );

        SpatialTexture diffuse = new SpatialTexture(model.getDiffuseTextureId(), model.getNormalMapTextureId(), 0);
        TexturedModel texturedModel = new TexturedModel(smodel, diffuse.getDiffuseTexture(), diffuse.getNormalMapTexture());

        objectEntity = new Entity(texturedModel, new Vector3f(0,0,0), 0,0,180,2f);
        objectBatches.put(texturedModel, List.of(objectEntity));

        light = new Light(new Vector3f(1f,1f,1f),new Vector3f(0,1,0));

    }

    private void loop() {
        GLFW.glfwSetScrollCallback(window, (w, x, y) -> camera.onScroll(y));

        while (!GLFW.glfwWindowShouldClose(window)) {
            renderer.beginFrame();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            camera.updateMouseLook();
            camera.updatePosition();
            camera.setTarget(objectEntity.getPosition());

            // Render
            shader.start();
            shader.loadProjectionMatrix(renderer.getProjectionMatrix());
            shader.loadViewMatrix(camera);
            shader.loadTextureUnits();
            shader.loadLight(light);

            renderer.render(objectBatches,shader);

            shader.stop();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }

    private void cleanUp() {
        loader.cleanUpVertexData();
        shader.cleanUp();
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
```
