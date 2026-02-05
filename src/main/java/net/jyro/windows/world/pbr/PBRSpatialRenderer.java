package net.jyro.windows.world.pbr;

import net.jyro.windows.world.camera.CameraView;
import net.jyro.windows.world.camera.CameraViewer;
import net.jyro.windows.world.light.Light;
import net.jyro.windows.world.pbr.model.AdvancedTexturedModel;
import net.jyro.windows.world.models.StaticModel;
import net.jyro.windows.world.pbr.entity.PBREntity;
import net.jyro.windows.world.shaders.QualityShader;
import net.jyro.windows.world.tools.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.*;

import java.util.List;
import java.util.Map;

public class PBRSpatialRenderer {

    private static final float FOV = 70f;
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000f;

    private final Matrix4f projectionMatrix;
    private final Matrix4f transformMatrix = new Matrix4f();

    /* ============================
       CONSTRUCTOR
       ============================ */

    public PBRSpatialRenderer(
        int framebufferWidth,
        int framebufferHeight,
        QualityShader shader
    ) {
        this.projectionMatrix = createProjectionMatrix(
            framebufferWidth,
            framebufferHeight
        );

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadTextureUnits();
        shader.stop();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /* ============================
       FRAME CONTROL
       ============================ */

    public void beginFrame() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /* ============================
       MAIN RENDER ENTRY
       ============================ */

    public void render(
        Map<AdvancedTexturedModel, List<PBREntity>> batches,
        QualityShader shader,
        CameraView camera,
        Light light
    ) {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadCameraPosition(camera.getPosition());
        shader.loadLight(light);

        for (AdvancedTexturedModel model : batches.keySet()) {
            prepareTexturedModel(model);

            for (PBREntity entity : batches.get(model)) {
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
    public void render(
        Map<AdvancedTexturedModel, List<PBREntity>> batches,
        QualityShader shader,
        CameraViewer camera,
        Light light
    ) {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadCameraPosition(camera.getPosition());
        shader.loadLight(light);

        for (AdvancedTexturedModel model : batches.keySet()) {
            prepareTexturedModel(model);

            for (PBREntity entity : batches.get(model)) {
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


    private void prepareTexturedModel(AdvancedTexturedModel model) {
        StaticModel smodel = model.getModel();

        GL30.glBindVertexArray(smodel.getVaoID());

        GL20.glEnableVertexAttribArray(0); // position
        GL20.glEnableVertexAttribArray(1); // uv
        GL20.glEnableVertexAttribArray(2); // normal
        GL20.glEnableVertexAttribArray(3); // tangent

        // Diffuse / Albedo
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getDiffuseTextureId());

        // Normal map
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getNormalMapTextureId());

        // Metallic-Roughness
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(
            GL11.GL_TEXTURE_2D,
            model.getMetallicRoughnessTextureId()
        );
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);

        GL30.glBindVertexArray(0);

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    private void prepareInstance(PBREntity entity, QualityShader shader) {
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
        shader.loadPBRFactors(
            entity.getMetallic(),
            entity.getRoughness()
        );
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
