package net.jyro.windows.world;

import net.jyro.windows.world.models.StaticModel;
import net.jyro.windows.world.models.TexturedModel;
import net.jyro.windows.world.models.entity.Entity;
import net.jyro.windows.world.shaders.DefaultShader;
import net.jyro.windows.world.shaders.QualityShader;
import net.jyro.windows.world.shaders.SunShader;
import net.jyro.windows.world.shaders.internal.ShaderDebugUI;
import net.jyro.windows.world.shaders.sims.PlanetaryShader;
import net.jyro.windows.world.tools.Maths;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;

import java.util.List;
import java.util.Map;

public class SpatialRenderer {

    private static final float FOV = 70f;
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000f;

    private final Matrix4f projectionMatrix;
    private final Matrix4f transformMatrix = new Matrix4f();

    private final int windowWidth;
    private final int windowHeight;

    public SpatialRenderer(DefaultShader shader) {
        long window = GLFW.glfwGetCurrentContext();
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetFramebufferSize(window, w, h);
        this.windowWidth = w[0];
        this.windowHeight = h[0];

        projectionMatrix = createProjectionMatrix(windowWidth, windowHeight);

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadTextureUnits();
        shader.stop();

        GL11.glClearColor(0,0,0, 1f);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    public void renderAtmospheres(Map<TexturedModel, List<Entity>> atmosphereBatches, PlanetaryShader shader) {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE); // additive
        GL11.glDepthMask(false); // prevent depth writes
        GL11.glCullFace(GL11.GL_FRONT); // render inside of sphere

        shader.start();

        for (TexturedModel model : atmosphereBatches.keySet()) {
            prepareTexturedModel(model);

            for (Entity entity : atmosphereBatches.get(model)) {
                prepareInstance(entity, shader); // transformation matrix
                GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    model.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0
                );
            }

            unbindTexturedModel();
        }

        shader.stop();

        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void renderPlanetsWithAtmosphere(
       Map<TexturedModel, List<Entity>> planetBatches,
       PlanetaryShader shader
    ) {
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glCullFace(GL11.GL_FRONT); // render inside of sphere


        shader.start();

        for (TexturedModel model : planetBatches.keySet()) {
            prepareTexturedModel(model);

            for (Entity entity : planetBatches.get(model)) {
                prepareInstance(entity, shader);
                GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    model.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0
                );
            }

            unbindTexturedModel();
        }

        shader.stop();
    }

    public void renderPlanets(Map<TexturedModel, List<Entity>> batches, DefaultShader shader) {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);

        render(batches, shader); // your existing render method
    }
    public void renderAtmosphereDebug(Map<TexturedModel, List<Entity>> planetBatches, PlanetaryShader shader, ShaderDebugUI debugUI) {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false); // don't write depth so the planet still renders in front

        shader.start();

        shader.loadPlanetRadii(debugUI.planetRadius, debugUI.atmosphereRadius);
        shader.loadSurfaceColor(debugUI.surfaceColor);
        shader.loadDensityFalloff(debugUI.densityFalloff);
        shader.loadNumSteps(debugUI.numInScattering);
        shader.loadIntensity(debugUI.intensity);

        for (TexturedModel model : planetBatches.keySet()) {
            prepareTexturedModel(model);
            for (Entity entity : planetBatches.get(model)) {
                prepareInstance(entity, shader);
                GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    model.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0
                );
            }
            unbindTexturedModel();
        }

        shader.stop();

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void renderSun(Map<TexturedModel, List<Entity>> batches, SunShader shader) {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glCullFace(GL11.GL_BACK);

        render(batches, shader); // your existing render method
    }

    public SpatialRenderer(PlanetaryShader shader) {
        long window = GLFW.glfwGetCurrentContext();
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetFramebufferSize(window, w, h);
        this.windowWidth = w[0];
        this.windowHeight = h[0];

        projectionMatrix = createProjectionMatrix(windowWidth, windowHeight);

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();

        GL11.glClearColor(0,0,0, 1f);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }


    public SpatialRenderer(QualityShader shader) {
        long window = GLFW.glfwGetCurrentContext();
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetFramebufferSize(window, w, h);
        this.windowWidth = w[0];
        this.windowHeight = h[0];

        projectionMatrix = createProjectionMatrix(windowWidth, windowHeight);

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadTextureUnits();
        shader.stop();

        GL11.glClearColor(0,0,0, 1f);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void beginFrame() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Map<TexturedModel, List<Entity>> batches, DefaultShader shader) {
        for (TexturedModel model : batches.keySet()) {
            prepareTexturedModel(model);

            for (Entity entity : batches.get(model)) {
                prepareInstance(entity, shader);
                GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    model.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0
                );
            }

            unbindTexturedModel();
        }
    }
    public void render(Map<TexturedModel, List<Entity>> batches,SunShader shader) {
        for (TexturedModel model : batches.keySet()) {
            prepareTexturedModel(model);

            for (Entity entity : batches.get(model)) {
                prepareInstance(entity, shader);
                GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    model.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0
                );
            }

            unbindTexturedModel();
        }
    }
    public void render(Map<TexturedModel, List<Entity>> batches, PlanetaryShader shader) {
        for (TexturedModel model : batches.keySet()) {
            prepareTexturedModel(model);

            for (Entity entity : batches.get(model)) {
                prepareInstance(entity, shader);
                GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    model.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0
                );
            }

            unbindTexturedModel();
        }
    }
    public void render(Map<TexturedModel, List<Entity>> batches, QualityShader shader) {
        for (TexturedModel model : batches.keySet()) {
            prepareTexturedModel(model);

            for (Entity entity : batches.get(model)) {
                prepareInstance(entity, shader);
                GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    model.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0
                );
            }

            unbindTexturedModel();
        }
    }

    public void prepareTexturedModel(TexturedModel model) {
        StaticModel smodel = model.getModel();

        GL30.glBindVertexArray(smodel.getVaoID());

        GL20.glEnableVertexAttribArray(0); // position
        GL20.glEnableVertexAttribArray(1); // texCoords
        GL20.glEnableVertexAttribArray(2); // normal
        GL20.glEnableVertexAttribArray(3); // tangent  <<< REQUIRED FOR GLTF

        // Diffuse texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(
            GL11.GL_TEXTURE_2D,
            model.getDiffuseTextureId()
        );

        // Normal map
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(
            GL11.GL_TEXTURE_2D,
            model.getNormalMapTextureId()
        );
    }

    public void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);

        GL30.glBindVertexArray(0);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    private void prepareInstance(Entity entity, DefaultShader shader) {
        transformMatrix.identity();
        Maths.createFastTransformationMatrix(
            entity.getPosition(),
            entity.getRotX(),
            entity.getRotY(),
            entity.getRotZ(),
            entity.getScale(),
            transformMatrix
        );
        shader.loadTransformationMatrix(transformMatrix);
    }
    private void prepareInstance(Entity entity, SunShader shader) {
        transformMatrix.identity();
        Maths.createFastTransformationMatrix(
            entity.getPosition(),
            entity.getRotX(),
            entity.getRotY(),
            entity.getRotZ(),
            entity.getScale(),
            transformMatrix
        );
        shader.loadTransformationMatrix(transformMatrix);
    }
    public void prepareInstance(Entity entity, PlanetaryShader shader) {
        transformMatrix.identity();
        Maths.createFastTransformationMatrix(
            entity.getPosition(),
            entity.getRotX(),
            entity.getRotY(),
            entity.getRotZ(),
            entity.getScale(),
            transformMatrix
        );
        shader.loadTransformationMatrix(transformMatrix);
    }
    private void unbindAdvancedTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);

        GL30.glBindVertexArray(0);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    private void prepareInstance(Entity entity,QualityShader shader) {
        transformMatrix.identity();
        Maths.createFastTransformationMatrix(
            entity.getPosition(),
            entity.getRotX(),
            entity.getRotY(),
            entity.getRotZ(),
            entity.getScale(),
            transformMatrix
        );
        shader.loadTransformationMatrix(transformMatrix);
    }

    private Matrix4f createProjectionMatrix(int width, int height) {
        float aspect = (float) width / (float) height;
        return new Matrix4f().perspective(
            (float) Math.toRadians(FOV),
            aspect,
            Z_NEAR,
            Z_FAR
        );
    }
}
